package com.robocubs4205.cubscout.scorecardsubmit;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.robocubs4205.cubscout.FieldScore;
import com.robocubs4205.cubscout.R;
import com.robocubs4205.cubscout.Scorecard;
import com.robocubs4205.cubscout.net.DaggerGsonComponent;
import com.robocubs4205.cubscout.net.NetModule;

import org.apache.commons.lang3.NotImplementedException;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by trevor on 1/8/17.
 */

public final class ScorecardSubmitActivity extends AppCompatActivity
        implements ScorecardSubmitView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.scorecard)
    RecyclerView scorecardView;
    ScorecardViewAdapter adapter;
    LinearLayoutManager layoutManager;
    @BindView(R.id.team_number_field)
    EditText teamNumberView;
    @BindView(R.id.match_number_field)
    EditText matchNumberView;
    @BindView(R.id.team_number_field_wrapper)
    TextInputLayout teamNumberViewWrapper;
    @BindView(R.id.match_number_field_wrapper)
    TextInputLayout matchNumberViewWrapper;
    private ScorecardSubmitPresenter presenter;
    private Scorecard scorecard;
    private Map<Integer, FieldScore> fieldScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = DaggerScorecardSubmitComponent.builder().scorecardSubmitModule(
                new ScorecardSubmitModule(this, this)).gsonComponent(DaggerGsonComponent.create())
                                                  .netModule(new NetModule(getApplicationContext()))
                                                  .build().presenter();

        setContentView(R.layout.activity_scorecard_submit);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new ScorecardViewAdapter();
        layoutManager = new LinearLayoutManager(this);
        scorecardView.setAdapter(adapter);
        scorecardView.setLayoutManager(layoutManager);
        presenter.initView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void setScorecard(Scorecard scorecard) {
        this.scorecard = scorecard;

    }

    @Override
    public void loadSavedScores(Map<Integer, FieldScore> scores) {
        fieldScores = scores;
        for (Integer index : scores.keySet()) {
            adapter.notifyItemChanged(index);
        }
    }

    @Override
    public void setTeamNumber(Integer teamNumber) {
        if (teamNumber != null) teamNumberView.setText(
                String.format(Locale.getDefault(), "%d", teamNumber));
    }

    @Override
    public void setMatchNumber(Integer matchNumber) {
        if (matchNumber != null) matchNumberView.setText(
                String.format(Locale.getDefault(), "%d", matchNumber));
    }

    @Override
    public void end() {
        finish();
    }

    @Override
    public void notifyMatchNumberMissing() {
        matchNumberViewWrapper.setError("Required");
    }

    @Override
    public void notifyTeamNumberMissing() {
        teamNumberViewWrapper.setError("Required");
    }

    @OnTextChanged(value = R.id.team_number_field,
                   callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onTeamNumberChanged(CharSequence text) {
        try {
            presenter.setTeamNumber(NumberFormat.getInstance(Locale.getDefault())
                                                .parse(text.toString()).intValue());
        }
        catch (ParseException e) {
            presenter.setTeamNumber(null);
        }
    }

    @OnTextChanged(value = R.id.match_number_field,
                   callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onMatchNumberChanged(CharSequence text) {
        try {
            presenter.setMatchNumber(NumberFormat.getInstance(Locale.getDefault())
                                                 .parse(text.toString()).intValue());
        }
        catch (ParseException e) {
            presenter.setMatchNumber(null);
        }
    }

    @OnClick(R.id.submit)
    void onSubmitClicked() {
        presenter.submit();
    }

    class ScorecardViewAdapter
            extends RecyclerView.Adapter<ScorecardViewAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(
                ViewGroup parent, int viewType) {
            return new ViewHolderFactory().Create(viewType, parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder,
                                     int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return scorecard.sections.size();
        }

        @Override
        public int getItemViewType(int position) {
            Scorecard.ScorecardSection section = scorecard.sections.get(position);
            if (section instanceof Scorecard.ScorecardTitleSection) {
                return R.layout.item_scorecard_title_section;
            }
            else if (section instanceof Scorecard.ScorecardParagraphSection) {
                return R.layout.item_scorecard_paragraph_section;
            }
            else if (section instanceof Scorecard.ScorecardNullableFieldSection) {
                switch (((Scorecard.ScorecardFieldSection) section).type) {
                    case COUNT:
                        return R.layout.item_scorecard_nullable_count_field_section;
                    case RATING:
                        return R.layout.item_scorecard_nullable_rating_field_section;
                    default:
                        throw new NotImplementedException("Unknown field section type");
                }
            }
            else if (section instanceof Scorecard.ScorecardFieldSection) {
                switch (((Scorecard.ScorecardFieldSection) section).type) {
                    case COUNT:
                        return R.layout.item_scorecard_count_field_section;
                    case RATING:
                        return R.layout.item_scorecard_rating_field_section;
                    default:
                        throw new NotImplementedException("Unknown field section type");
                }
            }
            else throw new NotImplementedException("Unknown scorecard section type");
        }

        abstract class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View itemView) {
                super(itemView);
            }

            protected abstract void bind(int position);
        }

        class TitleSectionViewHolder extends ViewHolder {

            @BindView(R.id.title)
            TextView titleView;

            public TitleSectionViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            @Override
            protected void bind(int position) {
                Scorecard.ScorecardTitleSection section =
                        (Scorecard.ScorecardTitleSection) scorecard.sections.get(position);
                titleView.setText(section.title);
            }
        }

        class ParagraphSectionViewHolder extends ViewHolder {

            @BindView(R.id.paragraph)
            TextView paragraphView;

            public ParagraphSectionViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            @Override
            protected void bind(int position) {
                Scorecard.ScorecardParagraphSection section =
                        (Scorecard.ScorecardParagraphSection) scorecard.sections.get(position);
                paragraphView.setText(section.paragraph);
            }
        }

        class CountFieldSectionViewHolder extends ViewHolder {

            private final View itemView;
            @BindView(R.id.value_wrapper)
            TextInputLayout valueWrapper;
            @BindView(R.id.value)
            EditText valueView;

            public CountFieldSectionViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                ButterKnife.bind(this, itemView);
            }

            @OnTextChanged(value = R.id.value, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
            void afterTextChanged(Editable text) {
                try {
                    if (text.toString().startsWith("0") && text.length() != 1) {
                        text.delete(0, 1);
                        return; //afterTextChanged called again if text is changed
                    }
                    if (text.length() == 0) {
                        text.insert(0, "0");
                        return; //afterTextChanged called again if text is changed
                    }
                    presenter.setFieldValue(
                            new FieldScore(getAdapterPosition(), NumberFormat.getInstance(
                                    Locale.getDefault()).parse(text.toString()).intValue()));
                    valueWrapper.setErrorEnabled(false);
                }
                catch (ParseException e) {
                    valueWrapper.setError("Not a valid number");
                    valueWrapper.setErrorEnabled(true);
                }
            }

            @OnClick(R.id.up_button)
            void onUpButtonClicked() {
                try {
                    int oldValue = NumberFormat
                            .getInstance(Locale.getDefault()).parse(valueView.getText().toString())
                            .intValue();
                    presenter.setFieldValue(new FieldScore(getAdapterPosition(), oldValue + 1));
                    valueView.setText(String.format(Locale.getDefault(), "%d", oldValue + 1));
                }
                catch (ParseException e) {
                    valueWrapper.setError("Not a valid number");
                    valueWrapper.setErrorEnabled(true);
                }
            }

            @OnClick(R.id.down_button)
            void onDownButtonClicked() {
                try {
                    int oldValue = NumberFormat
                            .getInstance(Locale.getDefault()).parse(valueView.getText().toString())
                            .intValue();
                    if (oldValue > 0) {
                        presenter.setFieldValue(new FieldScore(getAdapterPosition(), oldValue - 1));
                        valueView.setText(String.format(Locale.getDefault(), "%d", oldValue - 1));
                    }
                }
                catch (ParseException e) {
                    valueWrapper.setError("Not a valid number");
                    valueWrapper.setErrorEnabled(true);
                }
            }

            @Override
            protected void bind(int position) {
                Scorecard.ScorecardFieldSection section =
                        (Scorecard.ScorecardFieldSection) scorecard.sections.get(position);
                valueWrapper.setHint(section.name);
                valueView.setText(
                        String.format(Locale.getDefault(), "%d", fieldScores.get(position).value));
            }
        }

        class RatingFieldSectionViewHolder extends ViewHolder {

            private final View itemView;
            @BindView(R.id.rating_label)
            TextView ratingLabelView;
            @BindView(R.id.rating)
            RatingBar ratingBar;

            public RatingFieldSectionViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                ButterKnife.bind(this, itemView);
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating,
                                                boolean fromUser) {
                        presenter.setFieldValue(
                                new FieldScore(getAdapterPosition(), Math.round(rating)));
                    }
                });
            }

            @Override
            protected void bind(int position) {
                ratingLabelView.setText(
                        ((Scorecard.ScorecardFieldSection) scorecard.sections.get(position)).name);
                ratingBar.setRating(fieldScores.get(position).value);
            }
        }

        private class ViewHolderFactory {
            public ScorecardViewAdapter.ViewHolder Create(int viewType, ViewGroup parent) {
                switch (viewType) {
                    case R.layout.item_scorecard_title_section:
                        return new ScorecardViewAdapter.TitleSectionViewHolder(
                                LayoutInflater.from(parent.getContext())
                                              .inflate(R.layout.item_scorecard_title_section,
                                                       parent, false));
                    case R.layout.item_scorecard_paragraph_section:
                        return new ScorecardViewAdapter.ParagraphSectionViewHolder(
                                LayoutInflater.from(parent.getContext())
                                              .inflate(R.layout.item_scorecard_paragraph_section,
                                                       parent, false));
                    case R.layout.item_scorecard_count_field_section:
                        return new ScorecardViewAdapter.CountFieldSectionViewHolder(
                                LayoutInflater.from(parent.getContext())
                                              .inflate(R.layout.item_scorecard_count_field_section,
                                                       parent, false));
                    case R.layout.item_scorecard_rating_field_section:
                        return new ScorecardViewAdapter.RatingFieldSectionViewHolder(
                                LayoutInflater.from(parent.getContext())
                                              .inflate(R.layout.item_scorecard_rating_field_section,
                                                       parent, false));
                    default:
                        throw new NotImplementedException("");
                }
            }
        }
    }
}
