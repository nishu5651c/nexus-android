package com.nexus.app;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nexus.app.ui.home.HomeFragment;
import com.nexus.app.ui.messages.MessagesFragment;
import com.nexus.app.ui.workspaces.WorkspacesFragment;
import com.nexus.app.ui.notifications.NotificationsFragment;
import com.nexus.app.ui.profile.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		bottomNav = findViewById(R.id.bottom_navigation);
		bottomNav.setOnItemSelectedListener(item -> {
			switch (item.getItemId()) {
				case R.id.nav_home:
					openFragment(new HomeFragment());
					return true;
				case R.id.nav_messages:
					openFragment(new MessagesFragment());
					return true;
				case R.id.nav_workspaces:
					openFragment(new WorkspacesFragment());
					return true;
				case R.id.nav_notifications:
					openFragment(new NotificationsFragment());
					return true;
				case R.id.nav_profile:
					openFragment(new ProfileFragment());
					return true;
			}
			return false;
		});

		// default
		if (savedInstanceState == null) {
			bottomNav.setSelectedItemId(R.id.nav_home);
		}
    }

    private void openFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
