package com.example.aiya_test_3.incidents;

import static com.example.aiya_test_3.incidents.CardAdapter.CardViewHolder.Fixed_Height;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aiya_test_3.R;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    LayoutInflater mInflater;
    Context context;
    firebaseCardSource data;

    //Firebase Code
    DatabaseReference nRootDatabaseRef;
    DatabaseReference nNodeRef;
    public int positionL = 0;
    public CardAdapter(Context context, firebaseCardSource data) {
        this.context = context;
        this.data = data;
        mInflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    public class CardViewHolder extends RecyclerView.ViewHolder{

        private TextView hazardDescription, hazardName, hazardAddress;
        private ImageView hazardPicture;
        private View cardView;
        private View cardContent;
        int original_height = 0;

        public static final int Fixed_Height = 400;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cardView = itemView;
            hazardDescription = itemView.findViewById(R.id.hazardDescription);
            hazardName = itemView.findViewById(R.id.hazardName);
            hazardAddress = itemView.findViewById(R.id.hazardAddress);
            hazardPicture = itemView.findViewById(R.id.hazardPicture);
            cardContent = itemView.findViewById(R.id.card_content);
            ImageButton upvote = itemView.findViewById(R.id.upvote);

            // Minimize initial cards
            // Here, we need to use this ViewTreeObserver and onGlobalLayout
            // Basically, we need to get original height of the card so that we can minimize and expand it
            // But we cannot get the original height in the constructor because the card havent been built yet
            // So we postpone the measuring of the height until the card layout has been built
            // Then we get the original height and minimize the card
            cardView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    cardView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    original_height = cardView.getMeasuredHeight(); // Get original height here
                    ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
                    layoutParams.height = Fixed_Height;
                    cardView.setLayoutParams(layoutParams);
                }
            });

            // Add click listener to the card view
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleCard(cardView, original_height);
                    Log.d("Card Created","Card Created");
                }
            });

            //Listener for when user click upvote
            upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String getClickedItemName = hazardName.getText().toString();
                    IncidentObject Incident =  data.getIncidentObjectbyName(getClickedItemName);
                    Incident.setUpvotes(Incident.getUpvotes()+1);

                    final String node = "Incident Objects";
                    nRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
                    nNodeRef = nRootDatabaseRef.child(node).child(Incident.getHazardID()).child(Incident.getHazardName_Input());
                    nNodeRef.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            IncidentObject currentValue = mutableData.getValue(IncidentObject.class);
                            if (currentValue == null) {
                                mutableData.child("upvotes").setValue(1);
                            } else {
                                mutableData.child("upvotes").setValue(Incident.getUpvotes());
                            }

                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(
                                DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                            System.out.println("Transaction completed");
                        }});
                }
            });

        }
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
        Log.d("RecyclerView", "onCreateViewHolder()");
        View cardView = mInflater.inflate(R.layout.card_view, viewGroup,false);
        return new CardViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        String word = data.getHazardDescription(position);
        String hazardName = data.getHazardName(position);
        String hazardType = data.getHazardType(position);
        String hazardAddress = data.getHazardAddress(position);
        LatLng hazardLatLng = data.getHazardLatLng(position);
        StorageReference hazardPicture = data.getHazardImage(position);
        Log.d("RecyclerView", "onBindViewHolder(): " + word);
        holder.hazardDescription.setText(word);
        holder.hazardName.setText(hazardName);
        holder.hazardAddress.setText(hazardAddress);
        FireBaseUtils.downloadToImageView(holder.itemView.getContext(),hazardPicture,holder.hazardPicture);
    }

    @Override
    public int getItemCount() {
        return data.getSize(); // return the total number of data items.
    }

    int vHeight = 0; // instantiate card height

    public void toggleCard(View view, int original_height) {

        vHeight = original_height; // instantiate card height
        int height = view.getMeasuredHeight();
        ValueAnimator cardAnimation;
        if (view.getMeasuredHeight() == Fixed_Height) {
            // Card is minimized, expand it
            cardAnimation = ValueAnimator.ofInt(Fixed_Height, vHeight);
            cardAnimation.setDuration(500);
            cardAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (Integer) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                    layoutParams.height = value;
                    view.setLayoutParams(layoutParams);
                }
            });
        } else {
            // Card is expanded, minimize it
            cardAnimation = ValueAnimator.ofInt(vHeight, Fixed_Height);
            cardAnimation.setDuration(500);
            cardAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (Integer) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                    layoutParams.height = value;
                    view.setLayoutParams(layoutParams);
                }
            });

            cardAnimation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd( Animator animation) {
                }
            });
        }

        cardAnimation.start();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}

