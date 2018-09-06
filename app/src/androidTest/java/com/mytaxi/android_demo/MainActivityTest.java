package com.mytaxi.android_demo;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.view.WindowManager;

import com.mytaxi.android_demo.activities.AuthenticationActivity;
import com.mytaxi.android_demo.activities.MainActivity;
import com.mytaxi.android_demo.utils.storage.SharedPrefStorage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by DilshanF on 9/4/2018.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void setUp() throws Exception {

        final MainActivity activity = mActivityRule.getActivity();
        Runnable wakeUpDevice = new Runnable() {
            public void run() {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        };
        activity.runOnUiThread(wakeUpDevice);

        if (mActivityRule.getActivity().getSharedPrefStorage().loadUser() == null) {
            // Login to application with valid credentials
            onView(withId(R.id.edt_username))
                    .perform(typeText(AuthenticationActivityTest.USERNAME),
                            closeSoftKeyboard())
                    .check(matches(withText(AuthenticationActivityTest.USERNAME)));
            onView(withId(R.id.edt_password))
                    .perform(typeText(AuthenticationActivityTest.PASSWORD), closeSoftKeyboard());
            onView(allOf(withId(R.id.btn_login), withText("LOGIN"))).perform(click());
            Log.e("@Test", "Navigate to MainActivity page");
            //Wait for a constant time of 3 seconds to get the response from server for login
            Thread.sleep(3000);
        }
    }

    @Test
    public void verifyAutocompleteTextbox() throws Exception {
        // Type "sa" to trigger two suggestions.
        onView(ViewMatchers.withId(R.id.textSearch))
                .perform(typeText("sa"), closeSoftKeyboard());

        // Check that both suggestions are displayed.
        onView(withText("Sara Christensen"))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onView(withText("Sarah Scott"))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        // Tap on a suggestion.
        onView(withText("Sarah Scott"))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .perform(click());

        // By clicking on the auto complete term, the driver profile should be display.
        onView(withId(R.id.textViewDriverName))
                .check(matches(withText("Sarah Scott")));

        Log.e("@Test", "Performing Auto completion text view test");
    }

    @Test
    public void verifyCallDriverOption() throws Exception {
        //Clear the content
        onView(ViewMatchers.withId(R.id.textSearch)).perform(clearText());
        // Type "sa" to trigger suggestions.
        onView(ViewMatchers.withId(R.id.textSearch))
                .perform(typeText("sa"), closeSoftKeyboard());

        // Tap on a suggestion.
        onView(withText("Sarah Scott"))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .perform(click());

        //Click on dial icon
        onView(withId(R.id.fab)).perform(click());
        Log.e("@Test", "Performing call driver test");
    }

    @Test
    public void verifyDriverProfile() throws Exception {
        //TODO
    }
}
