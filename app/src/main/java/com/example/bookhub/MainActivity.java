package com.example.bookhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.bookhub.databinding.ActivityMainBinding;
import com.example.bookhub.fragments.AddBookFragment;
import com.example.bookhub.fragments.DashboardFragment;
import com.example.bookhub.fragments.ProfileFragment;
import com.example.bookhub.fragments.ChatsFragment;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new DashboardFragment());
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replaceFragment(new AddBookFragment());
            }
        });
        binding.bar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.menu_dashboard){
                    replaceFragment(new DashboardFragment());
                    return true;
                } else if (item.getItemId() == R.id.menu_chats) {
                    replaceFragment(new ChatsFragment());
                    return true;
                } else if (item.getItemId() == R.id.menu_profile) {
                    replaceFragment(new ProfileFragment());
                    return true;
                }
                return false;
            }
        });


    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

}