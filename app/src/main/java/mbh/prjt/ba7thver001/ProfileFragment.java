package mbh.prjt.ba7thver001;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.SendPhotoProfile} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static final int SELECT_PICTURE=1;

    private ImageView photoProfil;
    private TextView usernamProfile,bioView,ratingTotaltext,user1Name,user2Name,user1Review,user2Review;
    private MaterialCardView twiter,facbook,email,phone;
    private RatingBar ratingTotal,user1Rating,user2Rating;
    private ImageButton changeProfile,changeBio;
    private StorageReference mStorageRef;
    private StorageReference adminStorageRef;
    private StorageReference profilePhotoRef;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "admin";
    private static final String ARG_PARAM2 = "auth";

    // TODO: Rename and change types of parameters
    private Admin admin;
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();

    private SendPhotoProfile mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param admin Parameter 1.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(Admin admin) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, admin);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            admin = (Admin) getArguments().getSerializable(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
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
        ratingTotaltext=(TextView) view.findViewById(R.id.ratingtext);
        user1Name=(TextView) view.findViewById(R.id.user1);/////////////////////////////////////////////////////
        user2Name=(TextView) view.findViewById(R.id.user2);/////////////////////////////////////////////////////
        user1Review=(TextView) view.findViewById(R.id.user1review);////////////////////////////////////////////
        user2Review=(TextView) view.findViewById(R.id.user2review);//////////////////////////////////////////////
        ratingTotal=(RatingBar) view.findViewById(R.id.ratingpro);
        user1Rating=(RatingBar) view.findViewById(R.id.user1rate);///////////////////////////////////////
        user2Rating=(RatingBar) view.findViewById(R.id.user2rate);/////////////////////////////////////////////
        changeProfile=(ImageButton) view.findViewById(R.id.changeprofile);
        changeBio=(ImageButton) view.findViewById(R.id.changebio);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        usernamProfile.setText(admin.getSrvcProviderName());
        bioView.setText(admin.getSrvcProviderBio());


        ratingTotaltext.setText(String.format(Locale.ENGLISH,"%.2f/5",admin.getSrvcProviderRating()));

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        database.getReference("Users/Admins/"+mAuth.getCurrentUser().getUid()+"/srvcProviderRating").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ratingTotal.setRating(snapshot.getValue(Float.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mStorageRef = FirebaseStorage.getInstance().getReference();
        adminStorageRef=mStorageRef.child("AdminsFiles").child(mAuth.getCurrentUser().getUid());
        profilePhotoRef=adminStorageRef.child("profileimg.jpg");

        profilePhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(photoProfil);
            }
        });

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


    // TODO: Rename method, update argument and hook method into UI event
    public void onFileSelected(Admin admin) {
        if (mListener != null) {
            mListener.SendPhotoProfile(admin);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SendPhotoProfile) {
            mListener = (SendPhotoProfile) context;
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
    public interface SendPhotoProfile {
        // TODO: Update argument type and name
        void SendPhotoProfile(Admin admin);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE && data != null) {
            Uri selectedimage = data.getData();

            uploadImageToFirebase(selectedimage);
        }
    }

    public void uploadImageToFirebase(Uri image){

        profilePhotoRef.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profilePhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(photoProfil);
                        admin.setSrvcProviderProfilImage(uri.toString());

                        onFileSelected(admin);
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
}
