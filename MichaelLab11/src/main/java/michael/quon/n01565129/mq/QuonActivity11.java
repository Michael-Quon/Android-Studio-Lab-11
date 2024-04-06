// Michael Quon N01565129
package michael.quon.n01565129.mq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;

public class QuonActivity11 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 viewPager = findViewById(R.id.Mic_view_pager);
        TabLayout tabLayout = findViewById(R.id.Mic_tab_layout);

        Toolbar toolbar = findViewById(R.id.Mic_toolbar);
        setSupportActionBar(toolbar);

        // Create and set up adapter for ViewPager
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);

        // Connect TabLayout with ViewPager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Set tab text or icon here
            switch (position) {
                case 0:
                    tab.setIcon(R.drawable.michael_icon);
                    tab.setText(getString(R.string.michael));
                    break;
                case 1:
                    tab.setIcon(R.drawable.quon_icon);
                    tab.setText(getString(R.string.quon));
                    break;
                case 2:
                    tab.setIcon(R.drawable.n01565129_icon);
                    tab.setText(getString(R.string.n01565129));
                    break;
                default:
                    tab.setText(getString(R.string.tab) + (position + 1));
            }
        }).attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.Mic_menu_contacts) {
            openContacts();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void openContacts() {
        Intent intent = new Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI);
        startActivity(intent);
    }

}
