package michael.quon.n01565129.mq;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // Return fragment instance for each position
        switch (position) {
            case 0:
                return new Mi11chael();
            case 1:
                return new Qu22on();
            case 2:
                return new N0133565129();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // Return the number of tabs
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Return the title of each tab
        switch (position) {
            case 0:
                return "Tab 1";
            case 1:
                return "Tab 2";
            case 2:
                return "Tab 3";
            default:
                return null;
        }
    }
}
