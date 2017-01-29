package com.robocubs4205.cubscout.scorecardsubmit;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.robocubs4205.cubscout.Application;
import com.robocubs4205.cubscout.ApplicationComponent;
import com.robocubs4205.cubscout.Event;
import com.robocubs4205.cubscout.FieldScore;
import com.robocubs4205.cubscout.R;
import com.robocubs4205.cubscout.Scorecard;

import org.apache.commons.lang3.NotImplementedException;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.Optional;

import static com.robocubs4205.cubscout.Scorecard.ScorecardFieldSection;
import static com.robocubs4205.cubscout.Scorecard.ScorecardNullableFieldSection;
import static com.robocubs4205.cubscout.Scorecard.ScorecardNullableFieldSection.NullWhen;
import static com.robocubs4205.cubscout.Scorecard.ScorecardNullableFieldSection.NullWhen.CHECKED;
import static com.robocubs4205.cubscout.Scorecard.ScorecardNullableFieldSection.NullWhen.UNCHECKED;

public final class ScorecardSubmitActivity extends AppCompatActivity
        implements MVPView {

    @BindView(R.id.scorecard)
    RecyclerView scorecardView;
    ScorecardViewAdapter adapter;
    LinearLayoutManager layoutManager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.team_number_field)
    EditText teamNumberView;
    @BindView(R.id.match_number_field)
    EditText matchNumberView;
    @BindView(R.id.team_number_field_wrapper)
    TextInputLayout teamNumberViewWrapper;
    @BindView(R.id.match_number_field_wrapper)
    TextInputLayout matchNumberViewWrapper;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.event_spinner)
    Spinner ongoingEventSpinner;
    EventSpinnerAdapter eventSpinnerAdapter;
    private Presenter presenter;
    @Nullable
    private Scorecard scorecard;
    @Nullable
    private Map<Integer, FieldScore> fieldScores;
    private List<Event> ongoingEvents;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler();

        setContentView(R.layout.activity_scorecard_submit);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new ScorecardViewAdapter();
        layoutManager = new LinearLayoutManager(this);
        scorecardView.setAdapter(adapter);
        scorecardView.setLayoutManager(layoutManager);

        eventSpinnerAdapter = new EventSpinnerAdapter();
        ongoingEventSpinner.setAdapter(eventSpinnerAdapter);

        ApplicationComponent applicationComponent = ((Application) getApplication())
                .getApplicationComponent();
        presenter = DaggerComponent.builder().module(
                new Module(this)).applicationComponent(applicationComponent).build()
                                   .presenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
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
    public void setScorecard(@NonNull Scorecard scorecard) {
        this.scorecard = scorecard;
    }

    @Override
    public void loadSavedScores(@NonNull Map<Integer, FieldScore> scores) {
        fieldScores = scores;
        handler.post(() -> adapter.notifyDataSetChanged());
    }

    @SuppressWarnings("WrongThread")
    @Override
    public void setTeamNumber(final Integer teamNumber) {
        handler.post(() -> {
            if (teamNumber != null) teamNumberView.setText(
                    String.format(Locale.getDefault(), "%d", teamNumber));
        });
    }

    @SuppressWarnings("WrongThread")
    @Override
    public void setMatchNumber(final Integer matchNumber) {
        handler.post(() -> {
            if (matchNumber != null) matchNumberView.setText(String.format(
                    Locale.getDefault(), "%d", matchNumber));
        });
    }

    @Override
    public void end() {
        finish();
    }

    @SuppressWarnings("WrongThread")
    @Override
    public void notifyMatchNumberMissing() {
        handler.post(() -> {
            matchNumberView.setError("Required");
            scrollView.scrollTo(0, 0);
            matchNumberView.requestFocus();
        });
    }

    @SuppressWarnings("WrongThread")
    @Override
    public void notifyTeamNumberMissing() {
        handler.post(() -> {
            teamNumberView.setError("Required");
            scrollView.scrollTo(0, 0);
            teamNumberView.requestFocus();
        });
    }

    @Override
    public void setOngoingEvents(List<Event> events) {
        ongoingEvents = events;
        handler.post(() -> eventSpinnerAdapter.notifyDataSetChanged());
    }

    @OnTextChanged(value = R.id.team_number_field,
                   callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onTeamNumberChanged(Editable text) {
        try {
            presenter.setTeamNumber(NumberFormat.getInstance(Locale.getDefault())
                                                .parse(text.toString()).intValue());
        }
        catch (ParseException e) {
            presenter.setTeamNumber(null);
        }
        if (text.length() != 0) {
            teamNumberViewWrapper.setErrorEnabled(false);
            teamNumberView.setError(null);
        }
    }

    @OnTextChanged(value = R.id.match_number_field,
                   callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onMatchNumberChanged(Editable text) {
        try {
            presenter.setMatchNumber(NumberFormat.getInstance(Locale.getDefault())
                                                 .parse(text.toString()).intValue());
        }
        catch (ParseException e) {
            presenter.setMatchNumber(null);
        }
        if (text.length() != 0) {
            matchNumberViewWrapper.setErrorEnabled(false);
            matchNumberView.setError(null);
        }
    }

    @OnFocusChange(R.id.team_number_field)
    void onTeamNumberFocusChange(EditText text, boolean hasFocus) {
        if (!hasFocus && text.length() == 0) notifyTeamNumberMissing();
    }

    @OnFocusChange(R.id.match_number_field)
    void onMatchNumberFocusChange(EditText text, boolean hasFocus) {
        if (!hasFocus && text.length() == 0) notifyMatchNumberMissing();
    }

    @OnClick(R.id.submit)
    void onSubmitClicked() {
        presenter.submit();
    }

    @UiThread
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
            if (scorecard != null) {
                return scorecard.sections.size();
            }
            else return 0;
        }

        @Override
        public int getItemViewType(int position) {
            assert scorecard != null;
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
                assert scorecard != null;
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
                assert scorecard != null;
                Scorecard.ScorecardParagraphSection section =
                        (Scorecard.ScorecardParagraphSection) scorecard.sections.get(position);
                paragraphView.setText(section.paragraph);
            }
        }

        class CountFieldSectionViewHolder extends ViewHolder {
            @BindView(R.id.value_wrapper)
            TextInputLayout valueWrapper;
            @BindView(R.id.value)
            EditText valueView;

            public CountFieldSectionViewHolder(View itemView) {
                super(itemView);
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
                            new FieldScore(scorecard, getAdapterPosition(),
                                           NumberFormat.getInstance(
                                                   Locale.getDefault()).parse(text.toString())
                                                       .intValue(), false));
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
                assert scorecard != null;
                Scorecard.ScorecardFieldSection section =
                        (Scorecard.ScorecardFieldSection) scorecard.sections.get(position);
                valueWrapper.setHint(section.name);
                assert fieldScores != null;
                valueView.setText(
                        String.format(Locale.getDefault(), "%d", fieldScores.get(position).value));
            }
        }

        class RatingFieldSectionViewHolder extends ViewHolder {
            final boolean isNullable;
            @BindView(R.id.rating_label)
            TextView ratingLabelView;
            @BindView(R.id.rating)
            RatingBar ratingBar;
            @Nullable
            @BindView(R.id.container)
            View containerView;
            @Nullable
            @BindView(R.id.null_checkbox)
            CheckBox nullCheckbox;
            @Nullable
            @BindView(R.id.null_checkbox_label)
            TextView nullCheckboxLabel;
            NullWhen nullWhen;

            public RatingFieldSectionViewHolder(View itemView, final boolean isNullable) {
                super(itemView);
                this.isNullable = isNullable;
                ButterKnife.bind(this, itemView);
                ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
                    if (isNullable) {
                        assert nullCheckbox != null;
                        presenter.setFieldValue(
                                new FieldScore(scorecard, getAdapterPosition(),
                                               Math.round(rating), nullCheckbox.isChecked() !=
                                                       (nullWhen == UNCHECKED)));
                    }
                    else {
                        presenter.setFieldValue(
                                new FieldScore(scorecard, getAdapterPosition(),
                                               Math.round(rating), false));
                    }
                });
            }

            @Override
            protected void bind(int position) {
                assert scorecard != null;
                ScorecardFieldSection section =
                        (ScorecardFieldSection) scorecard.sections.get(position);
                ratingLabelView.setText(
                        section.name);
                assert fieldScores != null;
                FieldScore fieldScore = fieldScores.get(position);
                ratingBar.setRating(fieldScore.value);
                if (isNullable) {
                    ScorecardNullableFieldSection concreteSection =
                            (ScorecardNullableFieldSection) section;
                    if (containerView == null) {
                        throw new IllegalStateException(
                                "field section view holder created with isNullable set to true, " +
                                        "but a view with id 'container' was not found in the " +
                                        "provided view hierarchy");
                    }
                    if (fieldScore.isNull) {
                        containerView.setVisibility(View.GONE);
                    }
                    else {
                        containerView.setVisibility(View.VISIBLE);
                    }
                    nullWhen = concreteSection.nullWhen;
                    if (nullWhen == UNCHECKED != fieldScore.isNull) {
                        assert nullCheckbox != null;
                        nullCheckbox.setChecked(true);
                    }
                    assert nullCheckboxLabel != null;
                    nullCheckboxLabel.setText(concreteSection.checkBoxMessage);
                }
            }

            @Optional
            @OnCheckedChanged(R.id.null_checkbox)
            void onCheckChanged(boolean checked) {
                if (containerView == null) {
                    throw new IllegalStateException(
                            "field section view holder created with isNullable set to true, " +
                                    "but a view with id 'container' was not found in the " +
                                    "provided view hierarchy");
                }
                if ((nullWhen == CHECKED && checked) || (nullWhen == UNCHECKED && !checked)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        containerView.setVisibility(View.VISIBLE);
                        containerView.requestLayout();
                        containerView.setAlpha(1.0f);
                        containerView.setTranslationY(0);
                        containerView.animate().alpha(0.0f).translationY(-containerView.getHeight())
                                     .setDuration(300).setInterpolator(
                                new FastOutSlowInInterpolator())
                                     .setListener(new Animator.AnimatorListener() {
                                         @Override
                                         public void onAnimationStart(Animator animation) {

                                         }

                                         @Override
                                         public void onAnimationEnd(Animator animation) {
                                             containerView.setVisibility(View.GONE);
                                         }

                                         @Override
                                         public void onAnimationCancel(Animator animation) {

                                         }

                                         @Override
                                         public void onAnimationRepeat(Animator animation) {

                                         }
                                     }).start();
                    }
                    else containerView.setVisibility(View.GONE);
                    presenter.setFieldValue(new FieldScore(scorecard, getAdapterPosition(),
                                                           Math.round(ratingBar.getRating()),
                                                           true));
                }
                else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        containerView.setAlpha(0.0f);
                        containerView.animate().alpha(1.0f).translationY(0.0f)
                                     .setInterpolator(new FastOutSlowInInterpolator())
                                     .setListener(new Animator.AnimatorListener() {
                                         @SuppressLint("NewApi")
                                         @Override
                                         public void onAnimationStart(Animator animation) {
                                             containerView.setVisibility(View.VISIBLE);
                                             containerView.requestLayout();
                                             containerView.setTranslationY(
                                                     -containerView.getHeight());
                                         }

                                         @Override
                                         public void onAnimationEnd(Animator animation) {
                                         }

                                         @Override
                                         public void onAnimationCancel(Animator animation) {

                                         }

                                         @Override
                                         public void onAnimationRepeat(Animator animation) {

                                         }
                                     }).start();
                    }
                    else containerView.setVisibility(View.VISIBLE);
                    presenter.setFieldValue(new FieldScore(scorecard, getAdapterPosition(),
                                                           Math.round(ratingBar.getRating()),
                                                           false));
                }
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
                                                       parent, false), false);
                    case R.layout.item_scorecard_nullable_rating_field_section:
                        return new ScorecardViewAdapter.RatingFieldSectionViewHolder(
                                LayoutInflater.from(parent.getContext())
                                              .inflate(
                                                      R.layout.item_scorecard_nullable_rating_field_section,
                                                      parent, false), true);
                    default:
                        throw new NotImplementedException("");
                }
            }
        }
    }

    private class EventSpinnerAdapter extends ArrayAdapter {
        public EventSpinnerAdapter() {
            super(ScorecardSubmitActivity.this, 0);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(super.getContext())
                                            .inflate(android.R.layout.simple_spinner_dropdown_item,
                                                     parent, false);
            }
            ((TextView) convertView).setText(ongoingEvents.get(position).getName());
            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            return getView(position, convertView, parent);
        }

        @Override
        public int getCount() {
            return ongoingEvents == null ? 0 : ongoingEvents.size();
        }
    }
}
