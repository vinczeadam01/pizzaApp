package hu.mobil.pizzaapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hu.mobil.pizzaapp.MainActivity;
import hu.mobil.pizzaapp.R;
import hu.mobil.pizzaapp.fragments.CartFragment;
import hu.mobil.pizzaapp.models.Food;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    // Member variables.
    private ArrayList<Food> mCartData = new ArrayList<>();
    private Context mContext;
    private CartFragment mParent;

    public CartAdapter(Context context, ArrayList<Food> itemsData, CartFragment parent) {
        this.mCartData = itemsData;
        this.mContext = context;
        mParent = parent;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.list_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        // Get current sport.
        Food currentItem = mCartData.get(position);

        // Populate the textviews with data.
        holder.bindTo(currentItem);

    }

    @Override
    public int getItemCount() {
        return mCartData.size();
    }

    private void refreshView() {
        mCartData = MainActivity.listCart();
        this.notifyDataSetChanged();
        mParent.checkEmptyCart(mCartData.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Member Variables for the TextViews
        private TextView mTitleText;
        private TextView mPriceText;
        private ImageView mItemImage;
        private Food currentFood;

        ViewHolder(View itemView) {
            super(itemView);

            // Initialize the views.
            mTitleText = itemView.findViewById(R.id.itemTitle);
            mPriceText = itemView.findViewById(R.id.price);
            mItemImage = itemView.findViewById(R.id.itemImage);

            itemView.findViewById(R.id.deletebtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.getAcitivity().removeFromCart(currentFood);
                    CartAdapter.this.refreshView();

                }
            });

        }

        void bindTo(Food currentItem){
            currentFood = currentItem;
            mTitleText.setText(currentItem.getName());
            mPriceText.setText(Integer.toString(currentItem.getPrice()) + " Ft");

            mItemImage.setImageResource(mContext.getResources().getIdentifier(currentItem.getImageSrc(), "drawable", mContext.getPackageName()));
        }

    }
}
