package campus.ui.form;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormatSymbols;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import static javax.swing.GroupLayout.Alignment;
import static javax.swing.JSpinner.NumberEditor;

import campus.data.domain.Exam;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class ExamCreatorForm extends CreatorForm<Exam>
    implements ActionListener, ChangeListener {
    private enum Month {
        JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER,
        OCTOBER, NOVEMBER, DECEMBER;

        private static final String[] monthNames =
            new DateFormatSymbols().getMonths();

        @Override
        public String toString() {
            var i = 0;
            for (var month : values()) {
                if (this.equals(month)) {
                    return monthNames[i];
                }
                i++;
            }
            return super.toString();
        }
    }

    private static final long serialVersionUID = 1148820813298039734L;

    private final JSpinner yearSpinner, daySpinner, hourSpinner, minuteSpinner;
    private final JComboBox<Month> monthComboBox;

    private ExamCreatorForm(Date initialDate) {
        var layout = new GroupLayout(this);

        var dateLabel = new JLabel("Datum:");
        var timeLabel = new JLabel("Uhrzeit:");
        var dayMonthSeparatorLabel = new JLabel(".");
        var monthYearSeparatorLabel = new JLabel(".");
        var hourMinuteSeparatorLabel = new JLabel(":");

        var currentDate = new GregorianCalendar();
        currentDate.setTime(initialDate);
        var currentYear = currentDate.get(Calendar.YEAR);
        var currentMonth = currentDate.get(Calendar.MONTH);
        var currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

        var yearModel = new SpinnerNumberModel(
            currentYear, currentYear, currentYear+10, 1);
        yearSpinner = new JSpinner(yearModel);
        yearSpinner.setEditor(new NumberEditor(yearSpinner, "#"));
        yearSpinner.addChangeListener(this);

        var monthModel = new DefaultComboBoxModel<>(Month.values());
        monthModel.setSelectedItem(Month.values()[currentMonth]);
        monthComboBox = new JComboBox<>(monthModel);
        monthComboBox.addActionListener(this);

        daySpinner = new JSpinner(new SpinnerNumberModel(currentDay, 1, 31, 1));
        daySpinner.setEditor(new NumberEditor(daySpinner, "#00"));

        hourSpinner = new JSpinner(new SpinnerNumberModel(8, 0, 23, 1));
        hourSpinner.setEditor(new NumberEditor(hourSpinner, "#00"));
        minuteSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        minuteSpinner.setEditor(new NumberEditor(minuteSpinner, "#00"));

        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(
                    layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(dateLabel)
                        .addGroup(
                            layout.createSequentialGroup()
                                .addComponent(daySpinner)
                                .addComponent(dayMonthSeparatorLabel)
                                .addComponent(monthComboBox)
                                .addComponent(monthYearSeparatorLabel)
                                .addComponent(yearSpinner)))
                .addPreferredGap(
                    LayoutStyle.ComponentPlacement.UNRELATED,
                    GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(
                    layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(timeLabel)
                        .addGroup(
                            layout.createSequentialGroup()
                                .addComponent(hourSpinner)
                                .addComponent(hourMinuteSeparatorLabel)
                                .addComponent(minuteSpinner))));

        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.BASELINE)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(dateLabel)
                        .addComponent(timeLabel))
                    .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(daySpinner)
                        .addComponent(dayMonthSeparatorLabel)
                        .addComponent(monthComboBox)
                        .addComponent(monthYearSeparatorLabel)
                        .addComponent(yearSpinner)
                        .addComponent(hourSpinner)
                        .addComponent(hourMinuteSeparatorLabel)
                        .addComponent(minuteSpinner))));

        setLayout(layout);
        updateDayModel();
        updateOkayButton();
    }

    @Override
    public Exam getValue() {
        return new Exam(getCalendar().getTime());
    }

    @Override
    public boolean shouldEnableOkayButton() {
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateDayModel();
        updateOkayButton();
    }

    private GregorianCalendar getCalendar() {
        return new GregorianCalendar(
            (Integer) yearSpinner.getValue(),
            monthComboBox.getSelectedIndex(),
            (Integer) daySpinner.getValue(),
            (Integer) hourSpinner.getValue(),
            (Integer) minuteSpinner.getValue());
    }

    private void updateDayModel() {
        var year = (Integer) yearSpinner.getValue();
        var month = monthComboBox.getSelectedIndex();
        var calendar = new GregorianCalendar(year, month, 1);

        var day = (Integer) daySpinner.getValue();

        var maximumNumberOfDays = calendar.getActualMaximum(
            Calendar.DAY_OF_MONTH);

        var dayModel = (SpinnerNumberModel) daySpinner.getModel();
        dayModel.setValue(Math.min(day, maximumNumberOfDays));
        dayModel.setMaximum(maximumNumberOfDays);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        updateDayModel();
        updateOkayButton();
    }

    public static Optional<Exam> showDialog(Component parentComponent) {
        var currentDate = new GregorianCalendar().getTime();
        return showDialog(parentComponent, currentDate, "Prüfung anmelden");
    }

    public static Optional<Exam> showDialog(
        Component parentComponent, Date initialDate, String title) {
        return CreatorForm.showDialog(
            parentComponent, new ExamCreatorForm(initialDate), title);
    }
}
