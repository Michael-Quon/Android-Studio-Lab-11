// Michael Quon N01565129
package michael.quon.n01565129.mq;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
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
    public int getItemCount() {
        // Return the number of tabs
        return 3;
    }
}
