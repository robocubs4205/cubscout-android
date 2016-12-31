package com.robocubs4205.cubscout;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.robocubs4205.cubscout.net.RobocubsNetworkUtils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Contract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static com.robocubs4205.cubscout.Scorecard.ScorecardFieldSection;
import static com.robocubs4205.cubscout.Scorecard.ScorecardFieldSection.Type.COUNT;
import static com.robocubs4205.cubscout.Scorecard.ScorecardFieldSection.Type.RATING;
import static com.robocubs4205.cubscout.Scorecard.ScorecardNullableFieldSection;
import static com.robocubs4205.cubscout.Scorecard.ScorecardNullableFieldSection.NullWhen;
import static com.robocubs4205.cubscout.Scorecard.ScorecardNullableFieldSection.NullWhen.CHECKED;
import static com.robocubs4205.cubscout.Scorecard.ScorecardNullableFieldSection.NullWhen.UNCHECKED;
import static com.robocubs4205.cubscout.Scorecard.ScorecardParagraphSection;
import static com.robocubs4205.cubscout.Scorecard.ScorecardSection;
import static com.robocubs4205.cubscout.Scorecard.ScorecardTitleSection;

public class MatchSubmitActivity extends Activity {
    //private List<Scorecard> scorecards = new ArrayList<>();
    //private List<ScorecardSectionAdapter.Data> scorecardSectionAdapterData = new ArrayList<>();
    private List<ScorecardData> scorecardData = new ArrayList<>();
    private boolean isShowingNoGameMessage = false;
    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView scorecardView;
    private ScorecardSectionAdapter scorecardSectionAdapter;
    private StateFragment stateFragment;

    private AsyncGetGames task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_submit);

        FragmentManager manager = getFragmentManager();
        stateFragment = (StateFragment) manager.findFragmentByTag("data");
        if (stateFragment == null) {
            stateFragment = new StateFragment();
            manager.beginTransaction().add(stateFragment, "data").commit();
            task = new AsyncGetGames();
            task.execute();
        } else {
            scorecardData = stateFragment.getScorecardData();
        }
        Spinner gameTypeSpinner = (Spinner) findViewById(R.id.select_game_type_spinner);
        ArrayAdapter<CharSequence> gameTypeSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.game_types, android.R.layout.simple_spinner_item);
        gameTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameTypeSpinner.setAdapter(gameTypeSpinnerAdapter);

        gameTypeSpinner.setOnItemSelectedListener(new GameTypeSpinnerItemSelectedListener());
        scorecardView = (RecyclerView) findViewById(R.id.scorecard);
        if (scorecardSectionAdapter == null) {
            scorecardSectionAdapter = new ScorecardSectionAdapter(this, new ArrayList<ScorecardSectionAdapter.Data>());
        }
        scorecardView.setAdapter(scorecardSectionAdapter);
        scorecardView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stateFragment.setScorecardData(scorecardData);
    }

    private class AsyncGetGames extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.d("MatchSubmitActivity", "getting games");
            try {
                URL url = new URL(getResources().getString(R.string.get_current_events_url));
                HttpURLConnection connection = RobocubsNetworkUtils
                        .SendGetRequest(url, getApplicationContext());
                int httpResult = connection.getResponseCode();
                if (httpResult == HttpURLConnection.HTTP_OK) {
                    JSONObject result = new JSONObject(IOUtils.toString(connection.getInputStream()));
                    JSONArray errors = result.getJSONArray("errors");
                    JSONArray games = result.getJSONArray("games");
                    if (errors.length() == 0) {
                        Log.d("MatchSubmitActivity", "games found: " + games.length());
                        ParseJSONGames(games);
                        return true;
                    } else {
                        for (int i = 0; i < errors.length(); i++) {
                            Log.e("MatchSubmitActivity", "error while submitting: " + errors.get(i).toString());
                        }
                        return false;
                    }
                }
                else return false;
            } catch (IOException | JSONException | CertificateException | NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
                Log.e("MatchSubmitActivity", "exception while retrieving games", e);
                return false;
            }
        }

        private void ParseJSONGames(JSONArray games) throws JSONException {
            for (int i = 0; i < games.length(); i++) {
                JSONObject gameJSON = games.getJSONObject(i);
                Scorecard scorecard = new Scorecard();
                scorecard.id = gameJSON.getInt("id");
                scorecard.year = gameJSON.getInt("game_year");
                scorecard.type = gameJSON.getString("game_type");
                scorecard.name = gameJSON.getString("game_name");
                scorecard.sections = new ArrayList<>();
                JSONArray sections = gameJSON.getJSONArray("sections");
                scorecard.sections = ParseJSONScorecardSections(sections);
                scorecardData.add(new ScorecardData(scorecard, new ArrayList<ScorecardSectionAdapter.Data>()));
            }
        }

        private List<ScorecardSection> ParseJSONScorecardSections(JSONArray sections) throws JSONException {
            List<ScorecardSection> result = new ArrayList<>();
            for (int j = 0; j < sections.length(); j++) {
                JSONObject sectionJSON = sections.getJSONObject(j);
                String sectionType = sectionJSON.getString("section_type");
                switch (sectionType) {
                    case "field":
                        result.add(ParseJSONFieldSection(sectionJSON));
                        break;
                    case "title": {
                        ScorecardTitleSection section = new ScorecardTitleSection();
                        section.id = sectionJSON.getInt("id");
                        section.index = sectionJSON.getInt("index");
                        section.title = sectionJSON.getString("title");
                        result.add(section);
                        break;
                    }
                    case "paragraph": {
                        ScorecardParagraphSection section = new ScorecardParagraphSection();
                        section.id = sectionJSON.getInt("id");
                        section.index = sectionJSON.getInt("index");
                        section.paragraph = sectionJSON.getString("paragraph");
                        result.add(section);
                        break;
                    }
                }
            }
            return result;
        }

        private ScorecardFieldSection ParseJSONFieldSection(JSONObject sectionJSON) throws JSONException {
            Boolean isNullable = sectionJSON.getBoolean("is_nullable");
            if (isNullable) {
                ScorecardNullableFieldSection section = new ScorecardNullableFieldSection();
                section.id = sectionJSON.getInt("id");
                section.index = sectionJSON.getInt("index");
                section.name = sectionJSON.getString("field_name");
                String fieldType = sectionJSON.getString("type");
                switch (fieldType) {
                    case "Count":
                        section.type = COUNT;
                        break;
                    case "Rating":
                        section.type = RATING;
                        break;
                    default:
                        throw new IllegalStateException("Scorecard field section has illegal value \"" + fieldType + "\" for \"type\"");
                }
                section.isNullable = isNullable;
                String nullWhen = sectionJSON.getString("null_when");
                switch (nullWhen) {
                    case "Checked":
                        section.nullWhen = CHECKED;
                        break;
                    case "Unchecked":
                        section.nullWhen = UNCHECKED;
                        break;
                    default:
                        throw new IllegalStateException("Scorecard field section has illegal value \"" + nullWhen + "\" for \"null_when\"");
                }
                section.checkBoxMessage = sectionJSON.getString("checkbox_message");
                return section;
            } else {
                ScorecardFieldSection section = new ScorecardFieldSection();
                section.id = sectionJSON.getInt("id");
                section.index = sectionJSON.getInt("index");
                section.name = sectionJSON.getString("field_name");
                String fieldType = sectionJSON.getString("type");
                switch (fieldType) {
                    case "Count":
                        section.type = COUNT;
                        break;
                    case "Rating":
                        section.type = RATING;
                        break;
                    default:
                        throw new IllegalStateException("Scorecard field section has illegal value \"" + fieldType + "\" for \"type\"");
                }
                section.isNullable = isNullable;
                return section;
            }
        }
    }

    private class GameTypeSpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                if (scorecardData.isEmpty() && task != null) {
                    task.get();
                } else if (scorecardData.isEmpty() && task == null) {
                    task = new AsyncGetGames();
                    task.execute();
                    task.get();
                }
                String gameType = getResources().getStringArray(R.array.game_types)[position];
                List<ScorecardData> games = new ArrayList<>();
                for (ScorecardData innerScorecardData : scorecardData) {
                    if (innerScorecardData.mScorecard.type.equals(gameType)) {
                        games.add(innerScorecardData);
                    }
                }
                if ((games.isEmpty() && !isShowingNoGameMessage) || (!games.isEmpty() && isShowingNoGameMessage)) {
                    ((ViewSwitcher) findViewById(R.id.select_game_spinner_switcher)).showNext();
                    isShowingNoGameMessage = !isShowingNoGameMessage;
                } else {
                    GameTypeSpinnerArrayAdapter selectGameSpinnerAdapter = new GameTypeSpinnerArrayAdapter(MatchSubmitActivity.this, games);
                    selectGameSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Spinner gameSpinner = (Spinner) findViewById(R.id.select_game_spinner);
                    gameSpinner.setAdapter(selectGameSpinnerAdapter);
                    gameSpinner.setOnItemSelectedListener(new GameSpinnerItemSelectedListener());
                }
            } catch (InterruptedException | ExecutionException e) {
                Log.e("MatchSubmitActivity", "exception while retrieving games", e);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class GameSpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ScorecardData innerScorecardData = (ScorecardData) parent.getItemAtPosition(position);
            if (innerScorecardData.mAdapterData.size() == 0) {
                innerScorecardData.mAdapterData.addAll(ScorecardSectionAdapter.Data.makeListFromScorecard(innerScorecardData.mScorecard));
            }
            scorecardSectionAdapter.setData(innerScorecardData.mAdapterData);
            scorecardSectionAdapter.notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            scorecardSectionAdapter.setData(new ArrayList<ScorecardSectionAdapter.Data>());
            scorecardSectionAdapter.notifyDataSetChanged();
        }
    }

    private static class GameTypeSpinnerArrayAdapter extends ArrayAdapter<ScorecardData> {
        public GameTypeSpinnerArrayAdapter(Context context, List<ScorecardData> items) {
            super(context, 0, items);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.item_scorecard, parent, false);
            }
            TextView textView = (TextView) convertView.getRootView();
            @SuppressWarnings("ConstantConditions")
            Scorecard scorecard = getItem(position).mScorecard;
            //TODO: year localization
            textView.setText(scorecard.name + " (" + scorecard.year + ")");
            return convertView;
        }

        @NonNull
        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            return getView(position, convertView, parent);
        }
    }

    private static class ScorecardSectionAdapter extends RecyclerView.Adapter<ScorecardSectionAdapter.ScoreCardSectionViewHolder> {
        private List<Data> mData;
        private final Context mContext;


        public ScorecardSectionAdapter(Context context, List<Data> scorecardSections) {
            mData = scorecardSections;
            mContext = context;
        }

        public void setData(List<Data> data) {
            mData = data;
        }

        @Override
        public ScoreCardSectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return ScoreCardSectionViewHolder.createInstance(this, mContext, parent, viewType);
        }

        @Override
        public void onBindViewHolder(ScoreCardSectionViewHolder holder, int position) {
            Data data = mData.get(position);
            if (data.scorecardSectionDataHolder == null) {
                data.scorecardSectionDataHolder = holder.getNewDataHolder();
            }
            holder.initFromScorecardSection(mData.get(position).scorecardSection);
            holder.bindToDataHolder(data.scorecardSectionDataHolder);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        @Override
        public int getItemViewType(int position) {
            ScorecardSection section = mData.get(position).scorecardSection;
            return ScoreCardSectionViewHolder.getSectionViewType(section);
        }

        static abstract class ScoreCardSectionViewHolder extends RecyclerView.ViewHolder {
            private static final int TYPE_FIELD_COUNT_SECTION = 0;
            private static final int TYPE_NULLABLE_FIELD_COUNT_SECTION = 1;
            private static final int TYPE_FIELD_RATING_SECTION = 2;
            private static final int TYPE_NULLABLE_FIELD_RATING_SECTION = 3;
            private static final int TYPE_TITLE_SECTION = 4;
            private static final int TYPE_PARAGRAPH_SECTION = 5;

            @SuppressWarnings("unused")
            protected final ScorecardSectionAdapter mScorecardSectionAdapter;
            protected ScorecardSectionDataHolder mDataHolder;

            private static int getSectionViewType(ScorecardSection section) {
                if (section instanceof ScorecardFieldSection) {
                    if (section instanceof ScorecardNullableFieldSection) {
                        ScorecardFieldSection scorecardFieldSection = (ScorecardFieldSection) section;
                        if (scorecardFieldSection.type == COUNT) {
                            return TYPE_NULLABLE_FIELD_COUNT_SECTION;
                        } else if (scorecardFieldSection.type == RATING) {
                            return TYPE_NULLABLE_FIELD_RATING_SECTION;
                        } else {
                            throw new NotImplementedException("ScorecardFieldSection in ScorecardSectionAdapter has unknown type " + scorecardFieldSection.type);
                        }
                    } else {
                        ScorecardFieldSection scorecardFieldSection = (ScorecardFieldSection) section;
                        if (scorecardFieldSection.type == COUNT) {
                            return TYPE_FIELD_COUNT_SECTION;
                        } else if (scorecardFieldSection.type == RATING) {
                            return TYPE_FIELD_RATING_SECTION;
                        } else {
                            throw new NotImplementedException("ScorecardFieldSection in ScorecardSectionAdapter has unknown type " + scorecardFieldSection.type);
                        }
                    }
                } else if (section instanceof ScorecardParagraphSection) {
                    return TYPE_PARAGRAPH_SECTION;
                } else if (section instanceof ScorecardTitleSection) {
                    return TYPE_TITLE_SECTION;
                } else {
                    throw new NotImplementedException("ScorecardSection in ScorecardSectionAdapter is not an instance of ScorecardFieldSection, ScorecardParagraphSection, or ScorecardTitleSection");
                }
            }

            static final ScoreCardSectionViewHolder createInstance(ScorecardSectionAdapter scorecardSectionAdapter, Context context, ViewGroup parent, int viewType) {
                View view;
                switch (viewType) {
                    case TYPE_FIELD_COUNT_SECTION:
                        view = LayoutInflater.from(context).inflate(R.layout.item_scorecard_count_field_section, parent, false);
                        return new ScorecardFieldSectionViewHolder(view, scorecardSectionAdapter, new CountFieldViewSection(scorecardSectionAdapter, view));
                    case TYPE_FIELD_RATING_SECTION:
                        view = LayoutInflater.from(context).inflate(R.layout.item_scorecard_rating_field_section, parent, false);
                        return new ScorecardFieldSectionViewHolder(view, scorecardSectionAdapter, new RatingFieldViewSection(scorecardSectionAdapter, view));
                    case TYPE_NULLABLE_FIELD_COUNT_SECTION:
                        view = LayoutInflater.from(context).inflate(R.layout.item_scorecard_nullable_count_field_section, parent, false);
                        return new ScorecardNullableFieldSectionViewHolder(view, scorecardSectionAdapter, new CountFieldViewSection(scorecardSectionAdapter, view));
                    case TYPE_NULLABLE_FIELD_RATING_SECTION:
                        view = LayoutInflater.from(context).inflate(R.layout.item_scorecard_nullable_rating_field_section, parent, false);
                        return new ScorecardNullableFieldSectionViewHolder(view, scorecardSectionAdapter, new RatingFieldViewSection(scorecardSectionAdapter, view));
                    case TYPE_PARAGRAPH_SECTION:
                        view = LayoutInflater.from(context).inflate(R.layout.item_scorecard_paragraph_section, parent, false);
                        return new ScorecardParagraphSectionViewHolder(view,scorecardSectionAdapter);
                    case TYPE_TITLE_SECTION:
                        view = LayoutInflater.from(context).inflate(R.layout.item_scorecard_title_section, parent, false);
                        return new ScorecardTitleSectionViewHolder(view,scorecardSectionAdapter);
                    default:
                        throw new IllegalStateException("attempt to create a ScoreCardSectionViewHolder with unknown view type " + Integer.toString(viewType));
                }
            }

            protected ScoreCardSectionViewHolder(View itemView, ScorecardSectionAdapter scorecardSectionAdapter) {
                super(itemView);
                mScorecardSectionAdapter = scorecardSectionAdapter;
            }

            public abstract void initFromScorecardSection(ScorecardSection section);

            public void bindToDataHolder(@NonNull ScorecardSectionDataHolder dataHolder) {
                mDataHolder = dataHolder;
            }

            @NonNull
            @Contract(pure = true)
            public ScorecardSectionDataHolder getNewDataHolder() {
                return new ScorecardSectionDataHolder() {
                };
            }
        }
        static class ScorecardFieldSectionViewHolder extends ScoreCardSectionViewHolder {
            protected final FieldViewSection viewSection;

            public ScorecardFieldSectionViewHolder(View itemView, ScorecardSectionAdapter scorecardSectionAdapter, FieldViewSection viewSection) {
                super(itemView, scorecardSectionAdapter);
                this.viewSection = viewSection;
                viewSection.setViewHolder(this);
            }

            @Override
            public void initFromScorecardSection(ScorecardSection section) {
                viewSection.InitFromScorecardSection((ScorecardFieldSection) section);
            }

            @NonNull
            @Override
            public ScorecardSectionDataHolder getNewDataHolder() {
                return new MyScorecardSectionDataHolder(0);
            }

            @Override
            public void bindToDataHolder(@NonNull ScorecardSectionDataHolder dataHolder) {
                super.bindToDataHolder(dataHolder);
                viewSection.bindToDataHolder((MyScorecardSectionDataHolder) dataHolder);
            }

            public static class MyScorecardSectionDataHolder extends ScorecardSectionDataHolder {
                int score;

                public MyScorecardSectionDataHolder(int score) {
                    this.score = score;
                }
            }
        }
        static class ScorecardNullableFieldSectionViewHolder extends ScorecardFieldSectionViewHolder {
            private final View fieldContainer;
            private final CheckBox checkbox;
            private NullWhen nullWhen;

            private void setFieldContainerVisibility(boolean isChecked) {
                if (nullWhen == CHECKED && isChecked) {
                    fieldContainer.setVisibility(View.GONE);
                } else if (nullWhen == CHECKED) //&&!isChecked
                {
                    fieldContainer.setVisibility(View.VISIBLE);
                } else if (nullWhen == UNCHECKED && isChecked) {
                    fieldContainer.setVisibility(View.VISIBLE);
                } else //nullWhen==UNCHECKED&&!isChecked
                {
                    fieldContainer.setVisibility(View.GONE);
                }
            }

            public ScorecardNullableFieldSectionViewHolder(View itemView, ScorecardSectionAdapter scorecardSectionAdapter, FieldViewSection viewSection) {
                super(itemView, scorecardSectionAdapter, viewSection);
                fieldContainer = itemView.findViewById(R.id.container);
                checkbox = (CheckBox) itemView.findViewById(R.id.null_checkbox);
                checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setFieldContainerVisibility(isChecked);
                        ((MyScorecardSectionDataHolder) mDataHolder).mChecked = isChecked;
                    }
                });
            }

            @Override
            public void initFromScorecardSection(ScorecardSection section) {
                super.initFromScorecardSection(section);
                ScorecardNullableFieldSection concreteScorecardSection = (ScorecardNullableFieldSection) section;
                nullWhen = concreteScorecardSection.nullWhen;
                if (concreteScorecardSection.nullWhen == UNCHECKED) {
                    fieldContainer.setVisibility(View.GONE);
                }
                checkbox.setText(concreteScorecardSection.checkBoxMessage);
            }

            @Override
            public void bindToDataHolder(@NonNull ScorecardSectionDataHolder dataHolder) {
                super.bindToDataHolder(dataHolder);
                MyScorecardSectionDataHolder concreteDataHolder = (MyScorecardSectionDataHolder) dataHolder;
                viewSection.bindToDataHolder(concreteDataHolder);
                checkbox.setChecked(concreteDataHolder.mChecked);
                setFieldContainerVisibility(concreteDataHolder.mChecked);//onCheckChanged may not be called with above line, so visibility must be reset manually first time.
            }

            @NonNull
            @Override
            public ScorecardSectionDataHolder getNewDataHolder() {
                return new MyScorecardSectionDataHolder(0, false);
            }

            public static class MyScorecardSectionDataHolder extends ScorecardFieldSectionViewHolder.MyScorecardSectionDataHolder {
                public boolean mChecked;

                @SuppressWarnings("SameParameterValue")
                public MyScorecardSectionDataHolder(int score, boolean checked) {
                    super(score);
                    mChecked = checked;
                }
            }
        }
        static class ScorecardTitleSectionViewHolder extends ScoreCardSectionViewHolder {
            final TextView titleView;

            protected ScorecardTitleSectionViewHolder(View itemView, ScorecardSectionAdapter scorecardSectionAdapter) {
                super(itemView, scorecardSectionAdapter);
                titleView = (TextView) itemView.findViewById(R.id.title);
            }

            @Override
            public void initFromScorecardSection(ScorecardSection section) {
                ScorecardTitleSection concreteScorecardSection = (ScorecardTitleSection) section;
                titleView.setText(concreteScorecardSection.title);
            }
        }
        static class ScorecardParagraphSectionViewHolder extends ScoreCardSectionViewHolder {
            final TextView paragraphView;

            protected ScorecardParagraphSectionViewHolder(View itemView, ScorecardSectionAdapter scorecardSectionAdapter) {
                super(itemView, scorecardSectionAdapter);
                paragraphView = (TextView) itemView.findViewById(R.id.paragraph);
            }

            @Override
            public void initFromScorecardSection(ScorecardSection section) {
                ScorecardParagraphSection concreteScorecardSection = (ScorecardParagraphSection) section;
                paragraphView.setText(concreteScorecardSection.paragraph);
            }
        }

        static abstract class FieldViewSection {
            @SuppressWarnings("unused")
            protected final ScorecardSectionAdapter mScorecardSectionAdapter;
            @SuppressWarnings("unused")
            protected ScorecardFieldSectionViewHolder mViewHolder;
            protected ScorecardFieldSectionViewHolder.MyScorecardSectionDataHolder mDataHolder;

            public void bindToDataHolder(ScorecardFieldSectionViewHolder.MyScorecardSectionDataHolder dataHolder) {
                mDataHolder = dataHolder;
            }

            public abstract void InitFromScorecardSection(ScorecardFieldSection section);

            public FieldViewSection(ScorecardSectionAdapter scorecardSectionAdapter) {
                mScorecardSectionAdapter = scorecardSectionAdapter;
            }

            public void setViewHolder(ScorecardFieldSectionViewHolder viewHolder) {
                mViewHolder = viewHolder;
            }
        }
        static class CountFieldViewSection extends FieldViewSection {
            private final EditText fieldSectionValue;
            private final TextView fieldSectionLabel;

            public CountFieldViewSection(ScorecardSectionAdapter scorecardSectionAdapter, View view) {
                super(scorecardSectionAdapter);
                fieldSectionValue = (EditText) view.findViewById(R.id.value);
                fieldSectionLabel = (TextView) view.findViewById(R.id.value_label);
                fieldSectionValue.setText(String.format(Locale.getDefault(), "%d", 0));
                Button upButton = (Button) view.findViewById(R.id.up_button);
                Button downButton = (Button) view.findViewById(R.id.down_button);
                upButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            int oldValue = (NumberFormat.getNumberInstance(Locale.getDefault()).parse(fieldSectionValue.getText().toString())).intValue();
                            int newValue = oldValue + 1;
                            fieldSectionValue.setText(String.format(Locale.getDefault(), "%d", newValue));
                            mDataHolder.score = newValue;
                        } catch (ParseException e) {
                            Log.e("CountFieldViewSection", "exception while incrementing count", e);
                        }
                    }
                });
                downButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            int oldValue = (NumberFormat.getNumberInstance(Locale.getDefault()).parse(fieldSectionValue.getText().toString())).intValue();
                            if (oldValue > 0) {
                                int newValue = oldValue - 1;
                                fieldSectionValue.setText(String.format(Locale.getDefault(), "%d", newValue));
                                mDataHolder.score = newValue;
                            }
                        } catch (ParseException e) {
                            Log.e("CountFieldViewSection", "exception while decrementing count", e);
                        }
                    }
                });
            }

            @Override
            public void bindToDataHolder(ScorecardFieldSectionViewHolder.MyScorecardSectionDataHolder dataHolder) {
                super.bindToDataHolder(dataHolder);
                fieldSectionValue.setText(String.format(Locale.getDefault(), "%d", dataHolder.score));
            }

            @Override
            public void InitFromScorecardSection(ScorecardFieldSection section) {
                fieldSectionLabel.setText(section.name);
            }
        }
        static class RatingFieldViewSection extends FieldViewSection {
            private final RatingBar fieldSectionValue;
            private final TextView fieldSectionLabel;

            public RatingFieldViewSection(ScorecardSectionAdapter scorecardSectionAdapter, View view) {
                super(scorecardSectionAdapter);
                fieldSectionValue = (RatingBar) view.findViewById(R.id.rating);
                fieldSectionLabel = (TextView) view.findViewById(R.id.rating_label);
                fieldSectionValue.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        mDataHolder.score = Math.round(rating);
                    }
                });
            }

            @Override
            public void bindToDataHolder(ScorecardFieldSectionViewHolder.MyScorecardSectionDataHolder dataHolder) {
                super.bindToDataHolder(dataHolder);
                fieldSectionValue.setRating(dataHolder.score);
            }

            @Override
            public void InitFromScorecardSection(ScorecardFieldSection section) {
                fieldSectionLabel.setText(section.name);
            }
        }

        static abstract class ScorecardSectionDataHolder {

        }
        public static class Data {
            public ScorecardSectionDataHolder scorecardSectionDataHolder;
            public final ScorecardSection scorecardSection;

            public static List<Data> makeListFromScorecard(Scorecard scorecard) {
                List<Data> output = new ArrayList<>();
                for (ScorecardSection section : scorecard.sections) {
                    output.add(new Data(section));
                }
                return output;
            }

            public Data(ScorecardSection section) {
                this(section, null);
            }

            @SuppressWarnings("SameParameterValue")
            public Data(ScorecardSection section, ScorecardSectionDataHolder dataHolder) {
                scorecardSection = section;
                scorecardSectionDataHolder = dataHolder;
            }
        }
    }

    private static class ScorecardData {
        final List<ScorecardSectionAdapter.Data> mAdapterData;
        final Scorecard mScorecard;

        public ScorecardData(Scorecard scorecard, List<ScorecardSectionAdapter.Data> adapterData) {
            mScorecard = scorecard;
            mAdapterData = adapterData;
        }
    }

    public static class StateFragment extends Fragment {
        private List<ScorecardData> mScorecardData;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        public void setScorecardData(List<ScorecardData> data) {
            mScorecardData = data;
        }

        public List<ScorecardData> getScorecardData() {
            return mScorecardData;
        }
    }
}
