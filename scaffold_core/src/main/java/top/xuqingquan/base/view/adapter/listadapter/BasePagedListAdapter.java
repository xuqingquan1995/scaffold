package top.xuqingquan.base.view.adapter.listadapter;

import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import kotlin.jvm.functions.Function0;
import top.xuqingquan.R;
import top.xuqingquan.base.model.entity.NetworkStatus;
import top.xuqingquan.base.view.adapter.viewholder.BaseViewHolder;
import top.xuqingquan.base.view.adapter.viewholder.NetworkStateViewHolder;

/**
 * Created by 许清泉 on 2019-04-27 16:23
 * 带有网络状态刷新
 */
public abstract class BasePagedListAdapter<T> extends SimplePagedListAdapter<T> {

    private NetworkStatus networkStatus;
    private static final int NETWORK_STATE = -0x799;
    private static final int ITEM = -0x977;
    private Function0<?> retry;

    public BasePagedListAdapter(@NonNull Function0<?> retry, @NonNull DiffUtil.ItemCallback<T> diff) {
        super(diff);
        this.retry = retry;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == NETWORK_STATE) {
            return new NetworkStateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.scaffold_network_state, parent, false));
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (getItemViewType(position) == NETWORK_STATE) {
            ((NetworkStateViewHolder) holder).bind(networkStatus, v -> retry.invoke());
        } else {
            //noinspection unchecked
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
