package com.example.aiya_test_3.incidents;

import static com.example.aiya_test_3.incidents.CardAdapter.CardViewHolder.Fixed_Height;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.StorageReference;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private String LastClickPosition;

    LayoutInflater mInflater;
    Context context;
    firebaseCardSource CardData;

    //Firebase Code
    DatabaseReference nRootDatabaseRef;
    DatabaseReference nNodeRef;
    public int positionL = 0;
    private boolean CardClicked;

    private Double hazardLat;
    private Double hazardLng;
    private LatLng hazardLatLng;

    public CardAdapter(Context context, firebaseCardSource CardData) {
        this.context = context;
        this.CardData = CardData;
        mInflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    public class CardViewHolder extends RecyclerView.ViewHolder{

        private TextView hazardDescription, hazardName, hazardAddress;
        private ImageView hazardPicture;
        private View cardView;
        private View cardContent;
        int original_height = 0;

        public static final int Fixed_Height = 450;

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

                    // Set Last Click Position and inform Incident Activity it can  call movemap location to last clicked
                    setCardClicked(true);
                    updateLastClickPosition(hazardName);

                    // Expand/Minimize Card
                    toggleCard(cardView, original_height,hazardName);
                    Log.d("Card Created","Card Created");
                }
            });

            //Listener for when user click upvote
            upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String getClickedItemName = hazardName.getText().toString();
                    IncidentObject Incident =  CardData.getIncidentObjectbyName(getClickedItemName);
                    Incident.setUpvotes(Incident.getUpvotes()+1); // Increase upvote locally
                    updateFirebaseUpvoteValue(Incident);
                }
            });

        }
    }

    private void updateFirebaseUpvoteValue(IncidentObject Incident){
        final String node = context.getString(R.string.Incident_Objects);
        nRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
        nNodeRef = nRootDatabaseRef.child(node).child(Incident.getHazardID()).child("Incident");
        nNodeRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                IncidentObject currentValue = mutableData.getValue(IncidentObject.class);
                if (currentValue == null) {
                    mutableData.child("upvotes").setValue(0);
                } else {
                    mutableData.child("upvotes").setValue(Incident.getUpvotes());
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                System.out.println("Transaction completed");
            }});
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

        String word = CardData.getHazardDescription(position);
        String hazardName = CardData.getHazardName(position);
        String hazardType = CardData.getHazardType(position);
        String hazardAddress = CardData.getHazardAddress(position);
        LatLng hazardLatLng = CardData.getHazardLatLng(position);
        StorageReference hazardPicture = CardData.getHazardImage(position);
        Log.d("RecyclerView", "onBindViewHolder(): " + word);

        holder.hazardDescription.setText(word);
        holder.hazardName.setText(hazardName);
        holder.hazardAddress.setText(hazardAddress);

        FireBaseUtils.downloadToImageView(holder.itemView.getContext(),hazardPicture,holder.hazardPicture);
    }

    @Override
    public int getItemCount() {
        return CardData.getSize(); // return the total number of data items.
    }

    int vHeight = 0; // instantiate card height

    public void toggleCard(View view, int original_height, TextView hazardName) {

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

    public void updateLastClickPosition(TextView hazardName){
        LastClickPosition = hazardName.getText().toString();
        setClickedCardPosition();
    }

    public void setClickedCardPosition(){
        if(isCardClicked()){
            hazardLat = CardData.getIncidentObjectbyName(LastClickPosition).getHazardAddress_Lat();
            hazardLng = CardData.getIncidentObjectbyName(LastClickPosition).getHazardAddress_Long();
            hazardLatLng =  new LatLng(hazardLat,hazardLng);
        }
    }

    public LatLng getClickedCardPosition(){
        if(isCardClicked()){
            return hazardLatLng;
        }
        return null;
    }

    public boolean isCardClicked() {
        return CardClicked;
    }

    public void setCardClicked(boolean clicked) {
        CardClicked = clicked;
    }

}

