/*Marcin Strzeszyna o Programie do Adasia:
 * 
 * Ogolne zalozenie programu jest take, aby otwieral pliki .class i przeszukiwal czy jest tam uzyta jakas 
 * klasa, jesli jest wtedy aby podal jaka jest to klasa (abstrakcyjna, enum itp) i jej nazwe... W sumie 
 * powinien tez podawac sciezke do folderu, ale to jest nie wazne poniewaz nie umiem tego zrobic. 
 * 
 * W tej chwili mam program ktory jest w paczce: org.impressivecode.depress.metric.refaktoryzacja.
 * W nim jest otwieranie tych plikow .class. A takze wyszukiwanie tych klas i sciaganie z nich nazw.
 * Lecz jest on odpalany jako osobny program. A sama wtyczka ktora mam napisac powinna to robic. 
 * 
 * Jesli chodzi o KNIME to mam program ktory dziala i przeszukuje plki xml... Oczywiscie w tym programie
 * jest to niepotrzebne. Podczas uruchamiania wtyczki odpala sie dodatkowo program refaktoryzacji. ale
 * nie tak to powinno wygl¹daæ... 
 * 
 * Mam do Ciebie prosbe... Jestes bardziej doswiadczony odemnie. Prosze zobacz czy umialbys zrobic tak
 * aby w DialogComponentFileChooser mozna bylo wybrac pliki .class i dzialal jak refaktoryzacja. 
 * Tak aby mozna bylo operowac na danych w tabeli Knime, a nie w osobnym programie. 
 * 
 * Podsumowywujac. 
 * W tej chwili sa dawa programy. Wczyka KNIME otwierajaca XML i program Refaktoryzacja ktory robi to co 
 * powinna robic wtyczka. Trzeba zmienic wtyczke KNIME, zeby otwierala pliki .class.
 * 
 * Mozesz zobaczyæ to?
 * 
 * 
 */

package org.impressivecode.depress.metric.datasource;
import static org.impressivecode.depress.metric.datasource.DataSourceAdapterNodeModel.createFileChooserSettings;

import javax.swing.JFileChooser;


import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * 
 */
public class DataSourceAdapterNodeDialog extends DefaultNodeSettingsPane {

    private static final String HISTORY_ID = "depress.metric.datasource.historyid";

    protected DataSourceAdapterNodeDialog() {
        addDialogComponent(getFileChooserComponent());
       
    } 
    
    private DialogComponentFileChooser getFileChooserComponent() {
        return new DialogComponentFileChooser(createFileChooserSettings(), HISTORY_ID, JFileChooser.OPEN_DIALOG, true);
    } 

}
