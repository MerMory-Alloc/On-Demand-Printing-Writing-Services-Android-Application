package mbh.prjt.ba7thver001;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class ControleServices {

    AppCompatActivity activity;

    public ControleServices() {
    }


    public void sort(ArrayList<Service> arrayList,int itemSelected){
        Service service=new Service();
        boolean sorted = false;
        switch (itemSelected) {
            case 0:
                while(!sorted) {
                    sorted = true;
                    for (int i = 0; i < arrayList.size()-1; i++) {
                        if (new GetTime().convertFromStringToTimestamp(arrayList.get(i).getStartServiceTime()).before(
                                new GetTime().convertFromStringToTimestamp(arrayList.get(i+1).getStartServiceTime()))) {
                            service = arrayList.get(i);
                            arrayList.set(i,arrayList.get(i+1));
                            arrayList.set(i+1,service);
                            sorted = false;
                        }
                    }
                }
                break;

            case 1:
                service=null;
                sorted = false;
                while(!sorted) {
                    sorted = true;
                    for (int i = 0; i < arrayList.size()-1; i++) {
                        if (new GetTime().convertFromStringToTimestamp(arrayList.get(i).getStartServiceTime()).after(
                                new GetTime().convertFromStringToTimestamp(arrayList.get(i+1).getStartServiceTime()))) {
                            service = arrayList.get(i);
                            arrayList.set(i,arrayList.get(i+1));
                            arrayList.set(i+1,service);
                            sorted = false;
                        }
                    }
                }
                break;
            case 2:
                service=null;
                sorted = false;
                while(!sorted) {
                    sorted = true;
                    for (int i = 0; i < arrayList.size()-1; i++) {
                        if (arrayList.get(i).getPageNumber()<arrayList.get(i+1).getPageNumber()) {
                            service = arrayList.get(i);
                            arrayList.set(i,arrayList.get(i+1));
                            arrayList.set(i+1,service);
                            sorted = false;
                        }
                    }
                }
                break;
            case 3:
                service=null;
                sorted = false;
                while(!sorted) {
                    sorted = true;
                    for (int i = 0; i < arrayList.size()-1; i++) {
                        if (arrayList.get(i).getPageNumber()>arrayList.get(i+1).getPageNumber()) {
                            service = arrayList.get(i);
                            arrayList.set(i,arrayList.get(i+1));
                            arrayList.set(i+1,service);
                            sorted = false;
                        }
                    }
                }
                break;
        }


    }

    public void showSortDialog(int arrayId) {
        FragmentManager fm = activity.getSupportFragmentManager();
        OrderDialog alertDialog = OrderDialog.newInstance(activity.getResources().getString(R.string.sortby),arrayId);
        alertDialog.show(fm, "fragment_alert");
    }

    public void showSearchDialog() {
        FragmentManager fm = activity.getSupportFragmentManager();
        SearchingDialog search = SearchingDialog.newInstance(activity.getResources().getString(R.string.search));
        search.show(fm, "searchdialog");
    }

    public void showSearchByNameDialog() {
        FragmentManager fm = activity.getSupportFragmentManager();
        SearchDialog editNameDialogFragment = SearchDialog.newInstance(activity.getResources().getString(R.string.search));
        editNameDialogFragment.show(fm, "searchdialog");
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }
}
