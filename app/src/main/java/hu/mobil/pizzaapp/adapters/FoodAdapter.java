package hu.mobil.pizzaapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hu.mobil.pizzaapp.DetailsActivity;
import hu.mobil.pizzaapp.MainActivity;
import hu.mobil.pizzaapp.R;
import hu.mobil.pizzaapp.models.Food;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    // Member variables.
    private ArrayList<Food> mFoodData = new ArrayList<>();
    private ArrayList<Food> mFoodDataAll = new ArrayList<>();
    private Context mContext;
    private int lastPosition = -1;

    public FoodAdapter(Context context, ArrayList<Food> itemsData) {
        this.mFoodData = itemsData;
        this.mFoodDataAll = itemsData;
        this.mContext = context;
    }

    @NonNull
    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.list_food, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.ViewHolder holder, int position) {
        // Get current sport.
        Food currentItem = mFoodData.get(position);

        // Populate the textviews with data.
        holder.bindTo(currentItem);


        if(holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }

    }

    @Override
    public int getItemCount() {
        return mFoodData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Member Variables for the TextViews
        private TextView mTitleText;
        private TextView mDescriptionText;
        private TextView mPriceText;
        //private ImageView mItemImage;
        private Food currentFood;

        ViewHolder(View itemView) {
            super(itemView);

            // Initialize the views.
            mTitleText = itemView.findViewById(R.id.itemTitle);
            mDescriptionText = itemView.findViewById(R.id.subTitle);
            //mItemImage = itemView.findViewById(R.id.itemImage);
            mPriceText = itemView.findViewById(R.id.price);

            // Add to cart button onclick
            itemView.findViewById(R.id.add_to_cart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.addToCart(new Food(currentFood));
                }
            });

            // Card onclick
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), DetailsActivity.class);
                    intent.putExtra("data", currentFood);
                    itemView.getContext().startActivity(intent);
                }
            });

        }

        void bindTo(Food currentItem){
            currentFood = currentItem;
            mTitleText.setText(currentItem.getName());
            mDescriptionText.setText(currentItem.getDescription());
            mPriceText.setText(Integer.toString(currentItem.getPrice()) + " Ft");

            // Load the images into the ImageView using the Glide library.
            //Glide.with(mContext).load(currentItem.getImageResource()).into(mItemImage);
        }
    }
}
