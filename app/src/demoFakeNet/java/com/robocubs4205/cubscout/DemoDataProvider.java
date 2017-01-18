package com.robocubs4205.cubscout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.robocubs4205.cubscout.net.StubCubscoutApi;
import com.robocubs4205.cubscout.scorelist.Result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Action;

import static com.robocubs4205.cubscout.Scorecard.ScorecardFieldSection;
import static com.robocubs4205.cubscout.Scorecard.ScorecardFieldSection.Type.COUNT;
import static com.robocubs4205.cubscout.Scorecard.ScorecardFieldSection.Type.RATING;
import static com.robocubs4205.cubscout.Scorecard.ScorecardNullableFieldSection;
import static com.robocubs4205.cubscout.Scorecard.ScorecardNullableFieldSection.NullWhen.UNCHECKED;

/**
 * Created by trevor on 1/10/17.
 */
@ApplicationScope
public class DemoDataProvider extends StubCubscoutApi {
    DemoDataDbHelper dbHelper;

    @Inject
    public DemoDataProvider(Application application) {
        super();
        dbHelper = new DemoDataDbHelper(application);
    }
    public Scorecard getDemoScorecard() {
        Scorecard scorecard = new Scorecard();
        scorecard.id = 1;
        scorecard.sections = new ArrayList<>();
        scorecard.sections.add(new ScorecardFieldSection("Gears taken to airship", COUNT));
        scorecard.sections.add(new ScorecardNullableFieldSection("Low boiler effectiveness",
                                                                 RATING, UNCHECKED,
                                                                 "Used low boiler"));
        scorecard.sections.add(new ScorecardNullableFieldSection("High boiler effectiveness",
                                                                 RATING, UNCHECKED,
                                                                 "Used High boiler"));
        scorecard.sections.add(new ScorecardNullableFieldSection("Hopper effectiveness", RATING,
                                                                 UNCHECKED, "Used the hopper"));
        scorecard.sections.add(new ScorecardNullableFieldSection("Defense", RATING, UNCHECKED,
                                                                 "Did defense"));
        return scorecard;
    }

    @Override
    public Completable submitMatch(final Integer teamNumber, final Integer matchNumber,
                                   final Scorecard scorecard,
                                   final Collection<FieldScore> values) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                for (FieldScore score : values) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("`index`", score.scorecardIndex);
                    if (score.isNull) {
                        contentValues.putNull("score");
                    }
                    else {
                        contentValues.put("score", score.value);
                    }
                    contentValues.put("robot", teamNumber);
                    dbHelper.getWritableDatabase().insertOrThrow("FieldScores", null,
                                                                 contentValues);
                }
            }
        });
    }

    @Override
    public Observable<List<Result>> getResults(final Scorecard scorecard, final String[] orderBy) {
        return Observable.create(new ObservableOnSubscribe<List<Result>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Result>> e) throws Exception {
                Cursor cursor = dbHelper.getReadableDatabase()
                                        .query(DemoDataDbHelper.FIELD_SCORES,
                                               new String[]{"`robot`", "`index`",
                                                            "AVG(`score`) AS `score`"},
                                               null, null, "`robot`,`index`", null, null);
                List<Result> results = new ArrayList<>();
                while (cursor.moveToNext()) {
                    int teamNumber = cursor.getInt(cursor.getColumnIndexOrThrow("robot"));
                    List<FieldScore> fieldScores = new ArrayList<>();
                    if (cursor.isNull(cursor.getColumnIndexOrThrow("score"))) {
                        fieldScores.add(new FieldScore(scorecard, cursor.getInt(
                                cursor.getColumnIndexOrThrow("index")), 0, true));
                    }
                    else {
                        fieldScores.add(new FieldScore(scorecard, cursor.getInt(
                                cursor.getColumnIndexOrThrow("index")), cursor.getInt(
                                cursor.getColumnIndexOrThrow("score")), false));
                    }
                    while (cursor.moveToNext() && (cursor.getInt(
                            cursor.getColumnIndexOrThrow("robot")) == teamNumber)) {
                        if (cursor.isNull(cursor.getColumnIndexOrThrow("score"))) {
                            fieldScores.add(new FieldScore(scorecard, cursor.getInt(
                                    cursor.getColumnIndexOrThrow("index")), 0, true));
                        }
                        else {
                            fieldScores.add(new FieldScore(scorecard, cursor.getInt(
                                    cursor.getColumnIndexOrThrow("index")), cursor.getInt(
                                    cursor.getColumnIndexOrThrow("score")), false));
                        }
                    }
                    cursor.moveToPrevious();
                    results.add(new Result(scorecard, fieldScores, teamNumber));
                }
                Collections.sort(results, new ResultComparator(orderBy, scorecard));
                Collections.reverse(results);
                e.onNext(results);
                cursor.close();
            }
        });
    }

    private static class ResultComparator implements Comparator<Result> {
        private final String[] orderBy;
        private final Scorecard scorecard;

        public ResultComparator(String[] orderBy, Scorecard scorecard) {
            this.orderBy = orderBy;
            this.scorecard = scorecard;
        }

        @Override
        public int compare(Result o1, Result o2) {
            for (String string : orderBy) {
                if (string.equals("overall")) {
                    int total1 = 0;
                    int total2 = 0;
                    for (FieldScore fieldScore : o1.fieldScores) {
                        total1 += fieldScore.isNull ? 0 : fieldScore.value;
                    }
                    for (FieldScore fieldScore : o2.fieldScores) {
                        total2 += fieldScore.isNull ? 0 : fieldScore.value;
                    }
                    if (total1 > total2) return 1;
                    else if (total1 < total2) return -1;
                }
                else {
                    for (FieldScore fieldScore : o1.fieldScores) {
                        ScorecardFieldSection section =
                                (ScorecardFieldSection) scorecard.sections
                                        .get(fieldScore.scorecardIndex);
                        if (section.name.equals(string)) {
                            for (FieldScore fieldScore2 : o2.fieldScores) {
                                ScorecardFieldSection section2 =
                                        (ScorecardFieldSection) scorecard.sections
                                                .get(fieldScore2.scorecardIndex);
                                if (section2.name.equals(string)) {
                                    if (!fieldScore.isNull && fieldScore2.isNull) return 1;
                                    else if (fieldScore.isNull && !fieldScore2.isNull) return -1;
                                    else if (fieldScore.isNull && fieldScore.isNull) break;
                                    else if (fieldScore.value > fieldScore2.value) return 1;
                                    else if (fieldScore.value < fieldScore2.value) return -1;
                                    else break;
                                }
                            }
                            break;
                        }
                    }
                }
            }
            return 0;
        }
    }

    private class DemoDataDbHelper extends SQLiteOpenHelper {
        static final int DB_VERSION = 1;
        static final String DB_NAME = "demo_data.db";
        static final String FIELD_SCORES = "FieldScores";

        public DemoDataDbHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO: 1/16/17 get an orm
            db.execSQL("CREATE TABLE `" + FIELD_SCORES + "` (`id` INTEGER PRIMARY KEY,`name` " +
                               "TEXT, `index` INTEGER,`score` INTEGER,`robot` INTEGER);");
            SQLiteStatement statement = db.compileStatement(
                    "INSERT INTO `" + FIELD_SCORES + "` (`index`,`score`,`robot`) VALUES(?,?,?)");
            statement.bindLong(3, 4205);
            statement.bindLong(2, 2);
            statement.bindLong(1, 0);
            statement.executeInsert();
            statement.bindLong(2, 3);
            statement.bindLong(1, 1);
            statement.executeInsert();
            statement.bindLong(2, 5);
            statement.bindLong(1, 2);
            statement.executeInsert();
            statement.bindNull(2);
            statement.bindLong(1, 3);
            statement.executeInsert();
            statement.bindNull(2);
            statement.bindLong(1, 4);
            statement.executeInsert();

            statement.bindLong(3, 1318);
            statement.bindLong(2, 7);
            statement.bindLong(1, 0);
            statement.executeInsert();
            statement.bindLong(2, 0);
            statement.bindLong(1, 1);
            statement.executeInsert();
            statement.bindLong(2, 5);
            statement.bindLong(1, 2);
            statement.executeInsert();
            statement.bindLong(2, 3);
            statement.bindLong(1, 3);
            statement.executeInsert();
            statement.bindLong(2, 1);
            statement.bindLong(1, 4);
            statement.executeInsert();

            statement.bindLong(3, 4077);
            statement.bindLong(2, 0);
            statement.bindLong(1, 0);
            statement.executeInsert();
            statement.bindNull(2);
            statement.bindLong(1, 1);
            statement.executeInsert();
            statement.bindNull(2);
            statement.bindLong(1, 2);
            statement.executeInsert();
            statement.bindNull(2);
            statement.bindLong(1, 3);
            statement.executeInsert();
            statement.bindLong(2, 5);
            statement.bindLong(1, 4);
            statement.executeInsert();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
