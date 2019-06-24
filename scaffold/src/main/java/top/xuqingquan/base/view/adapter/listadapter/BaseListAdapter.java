package top.xuqingquan.base.view.adapter.listadapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.NotNull;
import top.xuqingquan.R;
import top.xuqingquan.base.model.entity.NetworkStatus;
import top.xuqingquan.base.view.adapter.viewholder.BaseViewHolder;
import top.xuqingquan.base.view.adapter.viewholder.NetworkStateViewHolder;

/**
 * Created by 许清泉 on 2019-04-27 16:23
 * 带有网络状态刷新
 */
public abstract class BaseListAdapter<T> extends SimpleListAdapter<T> {

    private NetworkStatus networkStatus;
    private static final int NETWORK_STATE = -1;
    private static final int ITEM = 1;
    private Function0 retry;

    public BaseListAdapter(@NotNull Function0 retry, @NotNull DiffUtil.ItemCallback<T> diff) {
        super(diff);
        this.retry = retry;
    }

    @NotNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == NETWORK_STATE) {
            return new NetworkStateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.network_state, parent, false));
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (getItemViewType(position) == NETWORK_STATE) {
            ((NetworkStateViewHolder) holder).bind(networkStatus, v -> retry.invoke());
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return NETWORK_STATE;
        } else {
            return ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + (hasExtraRow() ? 1 : 0);
    }

    public void setNetworkState(NetworkStatus newNetworkStatus) {
        NetworkStatus previousState = this.networkStatus;
        boolean hadExtraRow = hasExtraRow();
        this.networkStatus = newNetworkStatus;
        boolean hasExtraRow = hasExtraRow();
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount());
            } else {
                notifyItemInserted(super.getItemCount());
            }
        } else if (hasExtraRow && previousState != newNetworkStatus) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    private boolean hasExtraRow() {
        return networkStatus != null && networkStatus != NetworkStatus.SUCCESS;
    }
}
