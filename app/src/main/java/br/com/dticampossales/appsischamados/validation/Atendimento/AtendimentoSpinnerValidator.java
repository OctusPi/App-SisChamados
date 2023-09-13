package br.com.dticampossales.appsischamados.validation.Atendimento;

import java.util.Objects;

import br.com.dticampossales.appsischamados.widgets.Common.BaseSpinner;

public class AtendimentoSpinnerValidator {
    public static boolean validate(BaseSpinner spinner) {
        if (Objects.equals(spinner.getSelectedKey(), "0")) {
            spinner.enableError(true);
            return false;
        } else {
            spinner.enableError(false);
            return true;
        }
    }
}
