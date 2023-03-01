package mbh.prjt.ba7thver001;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Locale;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClientProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClientProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientProfileFragment extends Fragment {

    private static final int SELECT_PICTURE=1;

    private ImageView photoProfil;
    private TextView usernamProfile,bioView;
    private MaterialCardView twiter,facbook,email,phone;
    private ImageButton changeProfile,changeBio;
    private StorageReference mStorageRef;
    private StorageReference clientStorageRef;
    private StorageReference profilePhotoRef;
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "client";

    // TODO: Rename and change types of parameters
    private Client client;

    private OnFragmentInteractionListener mListener;

    public ClientProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param client Parameter 1.
     * @return A new instance of fragment ClientProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientProfileFragment newInstance(Client client) {
        ClientProfileFragment fragment = new ClientProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, client);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            client = (Client) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        photoProfil=(ImageView) view.findViewById(R.id.profilphoto);
        usernamProfile=(TextView) view.findViewById(R.id.usernameprofile);
        bioView=(TextView) view.findViewById(R.id.bio);
        twiter=(MaterialCardView) view.findViewById(R.id.twiter);
        facbook=(MaterialCardView) view.findViewById(R.id.facbook);
        email=(MaterialCardView) view.findViewById(R.id.email);
        phone=(MaterialCardView) view.findViewById(R.id.phone);
        changeProfile=(ImageButton) view.findViewById(R.id.changeprofile);
        changeBio=(ImageButton) view.findViewById(R.id.changebio);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        clientStorageRef=mStorageRef.child("ClientsFiles").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        profilePhotoRef=clientStorageRef.child("profileimg.jpg");

        profilePhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(photoProfil);
            }
        });


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        usernamProfile.setText(client.getServiceRequesterName());
        bioView.setText(client.getServiceRequesterBio());

        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE && data != null) {
            Uri selectedimage = data.getData();

            uploadImageToFirebase(selectedimage);
        }
    }

    private void uploadImageToFirebase(Uri image){

        profilePhotoRef.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profilePhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(photoProfil);
                        client.setServiceRequesterPhotoProfile(uri.toString());

                        onButtonPressed(client);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),R.string.couldntchangeprofilpicture,Toast.LENGTH_LONG).show();
            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Client client) {
        if (mListener != null) {
            mListener.onFragmentInteraction(client);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Client client);
    }
}
