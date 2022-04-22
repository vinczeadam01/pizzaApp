package hu.mobil.pizzaapp.adapters;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import hu.mobil.pizzaapp.DetailsActivity;
import hu.mobil.pizzaapp.MainActivity;
import hu.mobil.pizzaapp.R;
import hu.mobil.pizzaapp.fragments.FoodsFragment;
import hu.mobil.pizzaapp.models.Food;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> implements Filterable {
    // Member variables.
    private ArrayList<Food> mFoodData = new ArrayList<>();
    private ArrayList<Food> mFoodDataAll = new ArrayList<>();
    private Context mContext;
    private int lastPosition = -1;
    private ImageView mCopyImg;

    public FoodAdapter(Context context, ArrayList<Food> itemsData, ImageView copyImg) {
        this.mFoodData = itemsData;
        this.mFoodDataAll = itemsData;
        this.mContext = context;

        this.mCopyImg = copyImg;
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
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.side_in);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }

    }

    @Override
    public int getItemCount() {
        return mFoodData.size();
    }

    private void cartAnimate(ImageView foodImageView /*, Bitmap b */) {
        mCopyImg.setImageDrawable(foodImageView.getDrawable());
        mCopyImg.setVisibility(View.VISIBLE);
        int[] dest = new int[2];
        MainActivity.cartIconView.getLocationInWindow(dest);
        int[] start = new int[2];
        foodImageView.getLocationInWindow(start);
        mCopyImg.setX(start[0]);
        mCopyImg.setY(start[1]-foodImageView.getHeight());
        AnimatorSet animSetXY = new AnimatorSet();
        ObjectAnimator y = ObjectAnimator.ofFloat(mCopyImg, "translationY", mCopyImg.getY(), dest[1]-foodImageView.getHeight());
        ObjectAnimator x = ObjectAnimator.ofFloat(mCopyImg, "translationX", mCopyImg.getX(), dest[0]);
        ObjectAnimator sy = ObjectAnimator.ofFloat(mCopyImg, "scaleY", 0.8f, 0.1f);
        ObjectAnimator sx = ObjectAnimator.ofFloat(mCopyImg, "scaleX", 0.8f, 0.1f);
        animSetXY.playTogether(x, y, sx, sy);
        animSetXY.setDuration(650);
        animSetXY.start();
        //mCopyImg.setVisibility(View.GONE);

    }

    @Override
    public Filter getFilter() {
        return foodsFilter;
    }

    public void switchCategory(String categoryName) {
        this.foodsFilter.filter(categoryName);
    }

    private Filter foodsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Food> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (charSequence == null || charSequence.length() == 0) {
                results.count = mFoodDataAll.size();
                results.values = mFoodDataAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Food item : mFoodDataAll) {
                    if (item.getName().toLowerCase().contains(filterPattern) || item.getCategory().contains((filterPattern))) {
                        filteredList.add(item);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }


        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mFoodData = (ArrayList)filterResults.values;
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Member Variables for the TextViews
        private TextView mTitleText;
        private TextView mDescriptionText;
        private TextView mPriceText;
        private ImageView mItemImage;
        private Food currentFood;

        ViewHolder(View itemView) {
            super(itemView);

            // Initialize the views.
            mTitleText = itemView.findViewById(R.id.itemTitle);
            mDescriptionText = itemView.findViewById(R.id.subTitle);
            mItemImage = itemView.findViewById(R.id.itemImage);
            mPriceText = itemView.findViewById(R.id.price);

            // Add to cart button onclick
            itemView.findViewById(R.id.add_to_cart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.addToCart(new Food(currentFood));
                    FoodAdapter.this.cartAnimate(mItemImage);
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

            mItemImage.setImageResource(mContext.getResources().getIdentifier(currentItem.getImageSrc(), "drawable", mContext.getPackageName()));
        }
    }
}
