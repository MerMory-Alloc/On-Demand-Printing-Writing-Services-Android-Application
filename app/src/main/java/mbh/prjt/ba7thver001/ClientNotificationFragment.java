package mbh.prjt.ba7thver001;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClientNotificationFragment} interface
 * to handle interaction events.
 * Use the {@link ClientNotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientNotificationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    ListView notificationListView;
    NotificationsAdapter notificationsAdapter;
    private ArrayList<Notification> notifications;

    //private OnFragmentInteractionListener mListener;

    public ClientNotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClientNotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientNotificationFragment newInstance(ArrayList<Notification> param2) {
        ClientNotificationFragment fragment = new ClientNotificationFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            notifications = (ArrayList<Notification>) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_notification, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notificationListView=(ListView) view.findViewById(R.id.notifications);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        notificationsAdapter=new NotificationsAdapter(getContext(),R.id.notifications,notifications);
        notificationListView.setAdapter(notificationsAdapter);

        notificationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Notification curNot=notifications.get(position);

                if (curNot.getNotificationType().equals("refuse")){
                    Intent intent=new Intent(getActivity(),ClientRefusedActivity.class);
                    startActivity(intent);
                }
                else if (curNot.getNotificationType().equals("accept")){
                    Intent intent=new Intent(getActivity(),ClientOnWaitForPaimentOrderActivity.class);
                    startActivity(intent);
                }else if (curNot.getNotificationType().equals("finish")){
                    Intent intent=new Intent(getActivity(),ClientFinishedActivity.class);
                    startActivity(intent);
                }else if (curNot.getNotificationType().equals("refundRefused")){
                    Intent intent=new Intent(getActivity(),AboutUsActivity.class);
                    startActivity(intent);
                }else if (curNot.getNotificationType().equals("refundAccepted")){
                    Intent intent=new Intent(getActivity(),RefundAcceptedActivity.class);
                    intent.putExtra("service",curNot.getServiceId());
                    startActivity(intent);
                }
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
   /* public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
