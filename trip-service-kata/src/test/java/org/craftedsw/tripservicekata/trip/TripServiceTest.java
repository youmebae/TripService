package org.craftedsw.tripservicekata.trip;

import static org.craftedsw.tripservicekata.trip.UserBuilder.aUser;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.craftedsw.tripservicekata.exception.UserNotLoggedInException;
import org.craftedsw.tripservicekata.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TripServiceTest {
	
	private static final User GUEST = null;
	private static final User UNUSED_USER = null;
	private static final User REGISTERD_USER = new User();
	private static final User ANOTHER_USER = new User();
	private static final Trip TO_BRAZIL = new Trip();
	private static final Trip TO_LONDON = new Trip();
	
	@Mock TripDAO tripDAO;
	
	@InjectMocks @Spy private TripService tripService = new TripService();

	@Test(expected = UserNotLoggedInException.class)
	public void should_throw_an_exception_when_user_is_not_logged_in() throws Exception {
			
		tripService.getFriendTrip(UNUSED_USER, GUEST);
	} 
	
	@Test
	public void should_not_return_any_trips_when_users_are_not_friends() throws Exception {
		
		User friend = aUser()
				.friendsWith(ANOTHER_USER)
				.withTrips(TO_BRAZIL)
				.build();
		
		List<Trip> friendTrips = tripService.getFriendTrip(friend, REGISTERD_USER);
		
		assertThat(friendTrips.size(), is(0));
	}
	
	@Test
	public void should_return_friend_trips_when_users_are_friends() throws Exception {
		
		User friend = aUser()
				.friendsWith(ANOTHER_USER, REGISTERD_USER)
				.withTrips(TO_BRAZIL, TO_LONDON)
				.build();
		
		given(tripDAO.tripsBy(friend)).willReturn(friend.trips());
	
		List<Trip> friendTrips = tripService .getFriendTrip(friend, REGISTERD_USER);
		
		assertThat(friendTrips.size(), is(2));
	}

} 
 