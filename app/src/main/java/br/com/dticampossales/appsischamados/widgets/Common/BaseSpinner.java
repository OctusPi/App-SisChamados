package br.com.dticampossales.appsischamados.widgets.Common;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.SortedMap;

import br.com.dticampossales.appsischamados.R;

public class BaseSpinner {
    private final Context context;
    private final Spinner spinner;
    private final SortedMap<Integer, ArrayList<String>> optionsMap;
    private final ArrayList<String> options;

    public BaseSpinner(Context context, Spinner spinner, SortedMap<Integer, ArrayList<String>> optionsMap) {
        this.context = context;
        this.spinner = spinner;
        this.optionsMap = optionsMap;

        this.optionsMap.put(Integer.parseInt(context.getString(R.string.filter_id_default)),
                new ArrayList<>(Arrays.asList(
                        context.getString(R.string.filter_id_default),
                        context.getString(R.string.filter_name_default))));

        this.options = new ArrayList<>();

        for (Integer option : optionsMap.keySet()) {
            options.add(StringEscapeUtils.unescapeHtml4(Objects.requireNonNull(optionsMap.get(option)).get(1)));
        }
    }

    public void build() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                context, R.layout.spinner_item, options);

        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);

        spinner.setAdapter(arrayAdapter);
    }

    public String getSelectedKey() {
        Integer key = spinner.getSelectedItemPosition();
        return Objects.requireNonNull(optionsMap.get(key)).get(0);
    }

    public String getSelectedValue() {
        Integer key = spinner.getSelectedItemPosition();
        return Objects.requireNonNull(optionsMap.get(key)).get(1);
    }

    public void enableError(boolean isEnabled) {
        TextView view = (TextView) spinner.getSelectedView();
        if (isEnabled) {
            view.setError("");
        }
        else {
            view.setError(null);
        }
    }

    public void selectInitial() {
        spinner.setSelection(0);
    }
}