package hu.com.pillenzaehler15;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class MediAdapter extends RecyclerView.Adapter<MediAdapter.ViewHolder> {

    MainActivity mainActivity;

    public MediAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from (parent.getContext ())
                .inflate (R.layout.row_layout, parent, false);
        return new ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.medi = mainActivity.mediList.get (position);

        holder.medikamentTv.setText (holder.medi.name + mainActivity.einsatzarrayName[holder.medi.einsatz]);
        holder.medikamentTv.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                mainActivity.pendingMedi = holder.medi;
                for (Medi medi : mainActivity.mediList) {

                }
                mainActivity.proc_mediMutation ();
            }
        });

        if (holder.medi.seq < 0) {
            holder.seqTv.setText ("");
            holder.medikamentTv.setBackgroundColor (Parms.alarmWeak);
        } else {
            holder.seqTv.setText (Integer.toString (holder.medi.seq));
            if (holder.medi.farbIdxTemp == -1) {
                holder.medikamentTv.setBackgroundColor (Parms.mediFarbeIntDefault);
            } else {
                holder.medikamentTv.setBackgroundColor (Parms.mediFarbenInt[holder.medi.farbIdx]);
            }
        }

        holder.bestand_istEt.setText (String.format (mainActivity.locale, "%.2f", holder.medi.bestandIst));
        holder.bestand_istEt.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                mainActivity.pendingMedi = holder.medi;
                mainActivity.proc_kontrolle_launch ();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mainActivity.mediList.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView seqTv;
        public final TextView medikamentTv;
        public final TextView bestand_istEt;
        public Medi medi;

        public ViewHolder(View view) {
            super (view);
            seqTv = view.findViewById (R.id.seqTv);
            medikamentTv = view.findViewById (R.id.medikamentTv);
            bestand_istEt = view.findViewById (R.id.bestand_istEt);
        }

        @Override
        public String toString() {
            return super.toString () + " '" + medikamentTv.getText () + "'";
        }
    }
}
