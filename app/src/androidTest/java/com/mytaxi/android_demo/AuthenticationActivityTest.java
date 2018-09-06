package com.mytaxi.android_demo;


import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;


import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.mytaxi.android_demo.activities.AuthenticationActivity;
import com.mytaxi.android_demo.activities.MainActivity;

import java.util.Collection;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.allOf;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AuthenticationActivityTest {

    public static final String USERNAME = "crazydog335";
    public static final String PASSWORD = "venture";

    @Rule
    public ActivityTestRule<AuthenticationActivity> mActivityRule = new ActivityTestRule<>(
            AuthenticationActivity.class);

    @Before
    public void setUp() {
        final AuthenticationActivity activity = mActivityRule.getActivity();
        Runnable wakeUpDevice = new Runnable() {
            public void run() {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        };
        activity.runOnUiThread(wakeUpDevice);
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.mytaxi.android_demo",
                appContext.getPackageName());
    }

    @Test
    public void verifyUserNameTextHint() {
        onView(allOf(withId(R.id.edt_username), withHint("User Name")));
        Log.e("@Test", "Verify UserName Hint");
    }

    @Test
    public void verifyPasswordTextHint() {
        onView(allOf(withId(R.id.edt_username), withHint("Password")));
        Log.e("@Test", "Verify UserName Hint");
    }

    @Test
    public void verifyLoginButtonIsDisplayed() {
        onView(withId(R.id.btn_login)).check(matches(isDisplayed()));
        Log.e("@Test", "verify Login Button IsDisplayed");
    }

    @Test
    public void userNameIsEmpty() throws InterruptedException {
        //Clear userName and click on login  button.
        onView(withId(R.id.edt_username)).perform(clearText(),
                closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());

        Thread.sleep(1500);
        //Check the toast message
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("Login failed")))
                .check(matches(withEffectiveVisibility(
                        ViewMatchers.Visibility.VISIBLE
                )));

        Log.e("@Test", "Performing empty userName test");
    }

    @Test
    public void passwordIsEmpty() throws InterruptedException {
        //Type userName and clear password and click on login  button.
        onView(withId(R.id.edt_username)).perform(typeText(USERNAME));
        onView(withId(R.id.edt_password)).perform(clearText(), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());

        //Check the toast message
        Thread.sleep(1500);
        onView(allOf(withId(0x7f0800ae), withText("Login failed")))
                .check(matches(withText("Login failed")));

        Log.e("@Test", "Performing empty password test");
    }

    @Test
    public void loginFailed() throws Exception {
        //Type invalid userName and password  and click on login  button.
        onView(withId(R.id.edt_username))
                .perform(typeText(USERNAME));
        onView(withId(R.id.edt_password))
                .perform(typeText("DROP usertable;#"), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());

        //Check the toast message
        Thread.sleep(1500);
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("Login failed")))
                .check(matches(withEffectiveVisibility(
                        ViewMatchers.Visibility.VISIBLE
                )));
    }

    @Test
    public void loginSuccessfully() throws Exception {
        //Type valid userName and password with hidden text
        onView(withId(R.id.edt_username))
                .perform(typeText(USERNAME),
                        closeSoftKeyboard())
                .check(matches(withText(USERNAME)));
        onView(withId(R.id.edt_password))
                .perform(typeText(PASSWORD), closeSoftKeyboard())
                .check(matches(isPasswordHidden()));

        //Perform click action of view with id as login_btn and text as "Login"
        onView(allOf(withId(R.id.btn_login), withText("LOGIN"))).perform(click());
        Log.e("@Test", "Navigate to MainActivity page");

        //Wait for a constant time of 3 seconds to get the response from server for login
        Thread.sleep(3000);

        //Check if the activity is successfully finished
        assertTrue(mActivityRule.getActivity().isFinishing());

        // Check that application is in the correct activity
        Activity activity = getActivityInstance();
        boolean b = (activity instanceof MainActivity);
        assertTrue("Navigate to incorrect page..Please check the code", b);

        Log.e("@Test", "Performing login success test");
    }

    private Matcher<View> isPasswordHidden() {
        return new BoundedMatcher<View, EditText>(EditText.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("Password is not hidden");
            }

            @Override
            protected boolean matchesSafely(EditText item) {
                //returns true if password is hidden
                return item.getTransformationMethod() instanceof PasswordTransformationMethod;
            }
        };
    }

    //This method will return the current activity
    private Activity getActivityInstance() {
        final Activity[] activity = new Activity[1];
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Activity currentActivity = null;
                Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                if (resumedActivities.iterator().hasNext()) {
                    currentActivity = (Activity) resumedActivities.iterator().next();
                    activity[0] = currentActivity;
                }
            }
        });

        return activity[0];
    }
}
