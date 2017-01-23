package com.robocubs4205.cubscout.scorecardsubmit;

import android.support.v4.util.ArrayMap;

import com.robocubs4205.cubscout.Application;
import com.robocubs4205.cubscout.DemoDataProvider;
import com.robocubs4205.cubscout.FieldScore;
import com.robocubs4205.cubscout.Scorecard;

import org.awaitility.Duration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Observable;

import static com.robocubs4205.cubscout.scorecardsubmit.StatePersistor.PersistedClass;
import static org.awaitility.Awaitility.waitAtMost;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by trevor on 1/21/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class PresenterTests {

    @Mock
    MVPView view;
    @Mock
    Application application;
    @Mock
    StatePersistor persistor;
    @Mock
    DemoDataProvider api;
    Presenter presenter;

    @Before
    public void setUp() {
        when(persistor.serialize(ArgumentMatchers.<Integer, FieldScore>anyMap(),
                                 any(Scorecard.class), (Integer) or(anyInt(), isNull()),
                                 (Integer) or(anyInt(), isNull())))
                .thenReturn(Completable.complete());
        when(persistor.deserialize()).thenReturn(Observable.just(new PersistedClass(
                new ArrayMap<Integer, FieldScore>(), DemoDataProvider.getDemoScorecard(), 0, 0)));

        presenter = new Presenter(view, api, persistor);

        waitAtMost(Duration.ONE_SECOND).until(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return presenter.isInitialized();
            }
        });
        clearInvocations(view, api);
        reset(persistor);
    }

    @Test
    public void init() {
        when(persistor.deserialize()).thenReturn(Observable.just(new PersistedClass(
                new ArrayMap<Integer, FieldScore>(), DemoDataProvider.getDemoScorecard(), 0, 0)));

        Presenter presenter =
                new Presenter(view, new DemoDataProvider(application), persistor);

        verify(view, timeout(1000).times(1)).loadSavedScores(
                ArgumentMatchers.<Integer, FieldScore>anyMap());
        verify(view, timeout(1000).times(1)).setScorecard(any(Scorecard.class));
        verify(view, timeout(1000).times(1)).setMatchNumber(0);
        verify(view, timeout(1000).times(1)).setTeamNumber(0);
        verify(persistor, times(1)).deserialize();
    }

    @Test
    public void setMatchNumber_NonNullMatchNumber_CallsSerialize() {
        when(persistor.serialize(ArgumentMatchers.<Integer, FieldScore>anyMap(),
                                 any(Scorecard.class), (Integer) or(anyInt(), isNull()),
                                 (Integer) or(anyInt(), isNull())))
                .thenReturn(Completable.complete());

        presenter.setMatchNumber(5);

        verify(persistor, timeout(1000).times(1)).serialize(
                ArgumentMatchers.<Integer, FieldScore>anyMap(),
                any(Scorecard.class),
                (Integer) or(anyInt(), isNull()), eq(5));
        verifyNoMoreInteractions(persistor);
    }

    @Test
    public void setTeamNumber_NonNullTeamNumber_CallsSerialize() {
        when(persistor.serialize(ArgumentMatchers.<Integer, FieldScore>anyMap(),
                                 any(Scorecard.class), (Integer) or(anyInt(), isNull()),
                                 (Integer) or(anyInt(), isNull())))
                .thenReturn(Completable.complete());

        presenter.setTeamNumber(5);

        verify(persistor, timeout(1000).times(1)).serialize(
                ArgumentMatchers.<Integer, FieldScore>anyMap(),
                any(Scorecard.class), eq(5),
                (Integer) or(anyInt(), isNull()));
        verifyNoMoreInteractions(persistor);
    }

    @Test
    public void setMatchNumber_NullMatchNumber_CallsSerialize() {
        when(persistor.serialize(ArgumentMatchers.<Integer, FieldScore>anyMap(),
                                 any(Scorecard.class), (Integer) or(anyInt(), isNull()),
                                 (Integer) or(anyInt(), isNull())))
                .thenReturn(Completable.complete());

        presenter.setMatchNumber(null);

        verify(persistor, timeout(1000).times(1)).serialize(
                ArgumentMatchers.<Integer, FieldScore>anyMap(),
                any(Scorecard.class),
                (Integer) or(anyInt(), isNull()),
                (Integer) isNull());
        verifyNoMoreInteractions(persistor);
    }

    @Test
    public void setTeamNumber_NullTeamNumber_CallsSerialize() {
        when(persistor.serialize(ArgumentMatchers.<Integer, FieldScore>anyMap(),
                                 any(Scorecard.class), (Integer) or(anyInt(), isNull()),
                                 (Integer) or(anyInt(), isNull())))
                .thenReturn(Completable.complete());

        presenter.setTeamNumber(null);

        verify(persistor, timeout(1000).times(1)).serialize(
                ArgumentMatchers.<Integer, FieldScore>anyMap(),
                any(Scorecard.class),
                (Integer) isNull(),
                (Integer) or(anyInt(), isNull()));
        verifyNoMoreInteractions(persistor);
    }

    @Test
    public void submit_NonNullTeamAndMatchNumber_CallsClearCache() {
        when(persistor.serialize(ArgumentMatchers.<Integer, FieldScore>anyMap(),
                                 any(Scorecard.class), anyInt(), anyInt()))
                .thenReturn(Completable.complete());
        when(api.submitMatch(anyInt(), anyInt(), any(Scorecard.class),
                             ArgumentMatchers.<FieldScore>anyCollection()))
                .thenReturn(Completable.complete());

        presenter.setMatchNumber(0);
        presenter.setTeamNumber(0);

        clearInvocations(persistor);

        presenter.submit();

        verify(persistor, timeout(1000).times(1)).clearCache();
        verifyNoMoreInteractions(persistor);
    }

    @Test
    public void submit_NonNullTeamAndMAtchNumber_CallsSubmitMatch() {
        when(persistor.serialize(ArgumentMatchers.<Integer, FieldScore>anyMap(),
                                 any(Scorecard.class), anyInt(), anyInt()))
                .thenReturn(Completable.complete());
        when(api.submitMatch(anyInt(), anyInt(), any(Scorecard.class),
                             ArgumentMatchers.<FieldScore>anyCollection()))
                .thenReturn(Completable.complete());

        presenter.setTeamNumber(0);
        presenter.setTeamNumber(0);

        presenter.submit();
        verify(api, times(1)).submitMatch(eq(0), eq(0), any(Scorecard.class),
                                          ArgumentMatchers.<FieldScore>anyCollection());
    }

    @Test
    public void submit_NullTeamAndMatchNumber_NotifiesMissing() {
        when(persistor.serialize(ArgumentMatchers.<Integer, FieldScore>anyMap(),
                                 any(Scorecard.class), (Integer) or(anyInt(), isNull()),
                                 (Integer) or(anyInt(), isNull())))
                .thenReturn(Completable.complete());

        presenter.setMatchNumber(null);
        presenter.setTeamNumber(null);

        clearInvocations(view);

        presenter.submit();

        verify(view, times(1)).notifyMatchNumberMissing();
        verify(view, times(1)).notifyTeamNumberMissing();
        verifyNoMoreInteractions(view);
    }
}
