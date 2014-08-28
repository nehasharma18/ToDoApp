package com.example.nehasharma.todolist;

import android.widget.ArrayAdapter;
import java.util.List;
import android.app.Activity;
import android.widget.TextView;
import android.widget.CheckBox;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.Toast;

/**
 * Created by nehasharma on 8/18/14.
 */
public class CheckBoxArrayAdapter extends ArrayAdapter<Task> {


    private final List<Task> list;
    private final Activity context;

    public CheckBoxArrayAdapter(Activity context, List<Task> list) {
        super(context, R.layout.list_layout, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView text;
        protected CheckBox checkbox;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = null;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.list_layout, null);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.label);
            //viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);

            viewHolder.checkbox.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    Task task = (Task) viewHolder.checkbox
                            .getTag();
                    int pos = viewHolder.text.getLineCount();
                    if(buttonView.isChecked()) {
                        task.setTaskStatus(1);
                    } else {
                        task.setTaskStatus(0);
                    }
                    task.deleteTask();
                    list.remove(position);
                    notifyDataSetChanged();
                }
            });
            view.setTag(viewHolder);
            viewHolder.checkbox.setTag(list.get(position));
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText(list.get(position).getTaskText());
        holder.checkbox.setChecked((list.get(position).getTaskStatus() == 1)
                ?true:false);
        return view;
    }
}
