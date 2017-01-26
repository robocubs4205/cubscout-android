package com.robocubs4205.cubscout.scorecardsubmit;

import android.support.v4.util.ArrayMap;

import com.robocubs4205.cubscout.Application;
import com.robocubs4205.cubscout.Event;
import com.robocubs4205.cubscout.FieldScore;
import com.robocubs4205.cubscout.Scorecard;
import com.robocubs4205.cubscout.net.CubscoutAPI;
import com.robocubs4205.cubscout.net.FakeCubscoutApi;

import org.awaitility.Duration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Observable;

import static com.robocubs4205.cubscout.scorecardsubmit.StatePersistor.PersistedClass;
import static org.awaitility.Awaitility.waitAtMost;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

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
    FakeCubscoutApi api;
    Presenter presenter;

    @Before
    public void setUp() {
        when(persistor.serialize(ArgumentMatchers.anyMap(),
                                 any(Scorecard.class), (Integer) argThat(is(anything())),
                                 (Integer) argThat(is(anything())),
                                 ArgumentMatchers.anyList(),
                                 (Event) argThat(is(anything()))))
                .thenReturn(Completable.complete());
        Event stubEvent = new Event(0, "");
        ArrayList<Event> eventList = new ArrayList<>();
        eventList.add(stubEvent);
        when(persistor.deserialize()).thenReturn(Observable.just(new PersistedClass(
                new ArrayMap<>(), FakeCubscoutApi.getDemoScorecard(), 0, 0, eventList, 0)));
        CubscoutAPI.GetEventsResponse stubResponse = new CubscoutAPI.GetEventsResponse();
        stubResponse.errors = new ArrayList<>();
        stubResponse.events = new ArrayList<>();
        Event event = new Event(1, "Mock event");
        stubResponse.events.add(event);
        when(api.getOngoingEvents()).thenReturn(Observable.just(stubResponse));

        presenter = new Presenter(view, api, persistor);

        waitAtMost(Duration.ONE_SECOND).until(() -> {
            return presenter.isInitialized();
        });
        clearInvocations(view, api);
        reset(persistor);

        when(persistor.serialize(ArgumentMatchers.anyMap(),
                                 any(Scorecard.class), (Integer) argThat(is(anything())),
                                 (Integer) argThat(is(anything())),
                                 ArgumentMatchers.anyList(),
                                 (Event) argThat(is(anything()))))
                .thenReturn(Completable.complete());
    }

    @Test
    public void constructor() {
        ArrayList<Event> eventsList = new ArrayList<>();
        eventsList.add(new Event(0, ""));
        when(persistor.deserialize()).thenReturn(Observable.just(new PersistedClass(
                new ArrayMap<>(), FakeCubscoutApi.getDemoScorecard(), 0, 0,
                eventsList, 0)));

        @SuppressWarnings({"unused", "UnusedAssignment"}) Presenter presenter =
                new Presenter(view, new FakeCubscoutApi(application), persistor);

        verify(view, timeout(1000).times(1)).loadSavedScores(
                ArgumentMatchers.anyMap());
        verify(view, timeout(1000).times(1)).setScorecard(any(Scorecard.class));
        verify(view, timeout(1000).times(1)).setMatchNumber(0);
        verify(view, timeout(1000).times(1)).setTeamNumber(0);
        verify(persistor, times(1)).deserialize();
    }

    @Test
    public void setMatchNumber_NonNullMatchNumber_CallsSerialize() {
        presenter.setMatchNumber(5);

        verify(persistor, timeout(1000).times(1)).serialize(
                ArgumentMatchers.anyMap(),
                any(Scorecard.class),
                (Integer) argThat(is(anything())), eq(5), ArgumentMatchers.anyList(),
                (Event) argThat(is(anything())));
        verifyNoMoreInteractions(persistor);
    }

    @Test
    public void setTeamNumber_NonNullTeamNumber_CallsSerialize() {
        presenter.setTeamNumber(5);

        verify(persistor, timeout(1000).times(1)).serialize(
                ArgumentMatchers.anyMap(),
                any(Scorecard.class), eq(5),
                (Integer) argThat(is(anything())), ArgumentMatchers.anyList(),
                (Event) argThat(is(anything())));
        verifyNoMoreInteractions(persistor);
    }

    @Test
    public void setMatchNumber_NullMatchNumber_CallsSerialize() {
        presenter.setMatchNumber(null);

        verify(persistor, timeout(1000).times(1)).serialize(
                ArgumentMatchers.anyMap(),
                any(Scorecard.class),
                (Integer) argThat(is(anything())),
                isNull(), ArgumentMatchers.anyList(),
                (Event) argThat(is(anything())));
        verifyNoMoreInteractions(persistor);
    }

    @Test
    public void setTeamNumber_NullTeamNumber_CallsSerialize() {

        presenter.setTeamNumber(null);

        verify(persistor, timeout(1000).times(1)).serialize(
                ArgumentMatchers.anyMap(),
                any(Scorecard.class),
                isNull(),
                (Integer) argThat(is(anything())), ArgumentMatchers.anyList(),
                (Event) argThat(is(anything())));
        verifyNoMoreInteractions(persistor);
    }

    @Test(expected = NullPointerException.class)
    public void setFieldValue_NullFieldScore_ThrowsNPE() {
        //noinspection ConstantConditions
        presenter.setFieldValue(null);
    }

    @Test
    public void setFieldValue_NullFieldScore_DoesntCallSerialize() {
        try {
            //noinspection ConstantConditions
            presenter.setFieldValue(null);
        }
        catch (NullPointerException ignored) {
        }
        verify(persistor, never()).serialize(ArgumentMatchers.anyMap(),
                                             any(Scorecard.class),
                                             (Integer) argThat(is(anything())),
                                             (Integer) argThat(is(anything())),
                                             ArgumentMatchers.anyList(),
                                             (Event) argThat(is(anything())));

    }

    @Test
    public void setFieldValue_ValidFieldScore_CallsSerialize() {
        presenter.setFieldValue(new FieldScore(mock(Scorecard.class), 0, 0, false));

        verify(persistor, timeout(1000).times(1))
                .serialize(ArgumentMatchers.anyMap(), any(Scorecard.class),
                           (Integer) argThat(is(anything())),
                           (Integer) argThat(is(anything())), ArgumentMatchers.anyList(),
                           (Event) argThat(is(anything())));
    }

    @Test
    public void submit_NonNullTeamAndMatchNumber_CallsClearCache() {
        when(persistor.serialize(ArgumentMatchers.anyMap(),
                                 any(Scorecard.class), anyInt(), anyInt(),
                                 ArgumentMatchers.anyList(),
                                 (Event) argThat(is(anything()))))
                .thenReturn(Completable.complete());
        when(api.submitMatch(anyInt(), anyInt(), any(Scorecard.class),
                             ArgumentMatchers.anyCollection()))
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
        when(persistor.serialize(ArgumentMatchers.anyMap(),
                                 any(Scorecard.class), anyInt(), anyInt(),
                                 ArgumentMatchers.anyList(),
                                 (Event) argThat(is(anything()))))
                .thenReturn(Completable.complete());
        when(api.submitMatch(anyInt(), anyInt(), any(Scorecard.class),
                             ArgumentMatchers.anyCollection()))
                .thenReturn(Completable.complete());

        presenter.setTeamNumber(0);
        presenter.setTeamNumber(0);

        presenter.submit();
        verify(api, times(1)).submitMatch(eq(0), eq(0), any(Scorecard.class),
                                          ArgumentMatchers.anyCollection());
    }

    @Test
    public void submit_NullTeamAndMatchNumber_NotifiesMissing() {
        when(persistor.serialize(ArgumentMatchers.anyMap(),
                                 any(Scorecard.class), (Integer) argThat(is(anything())),
                                 (Integer) argThat(is(anything())),
                                 ArgumentMatchers.anyList(),
                                 (Event) argThat(is(anything()))))
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
