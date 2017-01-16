package com.robocubs4205.cubscout.scorelist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.robocubs4205.cubscout.Application;
import com.robocubs4205.cubscout.FieldScore;
import com.robocubs4205.cubscout.R;

import org.apache.commons.lang3.NotImplementedException;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.robocubs4205.cubscout.Scorecard.ScorecardFieldSection;

public class ScoreListActivity extends AppCompatActivity implements ScoreListView {

    ScoreListPresenter presenter;
    @BindView(R.id.team_list)
    RecyclerView teamListView;
    TeamListAdapter adapter;

    private List<Result> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_list);
        presenter = DaggerScoreListComponent.builder().applicationComponent(
                ((Application) getApplication()).getApplicationComponent())
                                            .scoreListModule(new ScoreListModule(this)).build()
                                            .presenter();
        ButterKnife.bind(this);
        adapter = new TeamListAdapter();
        presenter.initView();
    }

    public void LoadResults(List<Result> results) {
        this.results = results;
        adapter.notifyDataSetChanged();
    }

    class TeamListAdapter extends RecyclerView.Adapter<TeamListAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            return new ViewHolderFactory().create(viewType, parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder,
                                     int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return results.size() * (results.get(0).fieldScores.size() + 1);
        }

        @Override
        public int getItemViewType(int position) {
            if (position % (results.get(0).fieldScores.size() + 1) == 0) {
                return R.layout.item_result_header;
            }
            else {
                ScorecardFieldSection section = (ScorecardFieldSection) results.get(
                        position / (results.get(0).fieldScores.size() + 1)).scorecard.sections.get(
                        position % (results.get(0).fieldScores.size() + 1));
                if (section.type == ScorecardFieldSection.Type.COUNT) {
                    return R.layout.item_result_count_row;
                }
                else if (section.type == ScorecardFieldSection.Type.RATING) {
                    return R.layout.item_result_rating_row;
                }
                else throw new NotImplementedException(
                            "unknown field section type when deciding view holder type");
            }
        }

        abstract class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View itemView) {
                super(itemView);
            }

            abstract void bind(int position);
        }

        class RowHeaderViewHolder extends ViewHolder {

            @BindView(R.id.team_number)
            TextView teamNumberView;

            public RowHeaderViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            @Override
            void bind(int position) {
                Result result = results.get(position / (results.get(0).fieldScores.size() + 1));
                teamNumberView.setText(result.teamNumber);
            }
        }

        class CountRowViewHolder extends ViewHolder {
            @BindView(R.id.field_label)
            TextView fieldLabelView;
            @BindView(R.id.field_value)
            TextView fieldValueView;

            public CountRowViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            @Override
            void bind(int position) {
                Result result = results.get(position / (results.get(0).fieldScores.size() + 1));
                FieldScore score =
                        result.fieldScores.get(position % (results.get(0).fieldScores.size() + 1));
                ScorecardFieldSection section =
                        (ScorecardFieldSection) score.scorecard.sections.get(score.scorecardIndex);
                fieldLabelView.setText(section.name);
                fieldValueView.setText(String.format(Locale.getDefault(), "%d", score.value));
            }
        }

        class RatingRowViewHolder extends ViewHolder {
            @BindView(R.id.field_label)
            TextView fieldLabelView;
            @BindView(R.id.field_value)
            RatingBar fieldValueView;

            public RatingRowViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            @Override
            void bind(int position) {
                Result result = results.get(position / (results.get(0).fieldScores.size() + 1));
                FieldScore score =
                        result.fieldScores.get(position % (results.get(0).fieldScores.size() + 1));
                ScorecardFieldSection section =
                        (ScorecardFieldSection) score.scorecard.sections.get(score.scorecardIndex);
                fieldLabelView.setText(section.name);
                fieldValueView.setRating(score.value);
            }
        }

        class ViewHolderFactory {
            ViewHolder create(int viewType, ViewGroup parent) {
                switch (viewType) {
                    case R.layout.item_result_count_row:
                        return new CountRowViewHolder(
                                LayoutInflater.from(parent.getContext())
                                              .inflate(R.layout.item_result_count_row, parent,
                                                       false));
                    case R.layout.item_result_header:
                        return new RowHeaderViewHolder(
                                LayoutInflater.from(parent.getContext())
                                              .inflate(R.layout.item_result_count_row, parent,
                                                       false));
                    case R.layout.item_result_rating_row:
                        return new RatingRowViewHolder(
                                LayoutInflater.from(parent.getContext())
                                              .inflate(R.layout.item_result_count_row, parent,
                                                       false));
                    default:
                        throw new NotImplementedException(
                                "unknown viewtype when constructing View holder");
                }
            }
        }
    }
}
