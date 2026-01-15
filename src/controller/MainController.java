package src.controller;

import src.model.LernAnwendungModel;
import src.model.Vokabeldatensatz;
import src.view.*;

import javax.swing.*;
import java.awt.*;

public class MainController {
    private final LernAnwendungModel model;

    private MainFrame frame;

    // Views
    private HomeView homeView;
    private VerwaltungView verwaltungView;
    private QuizView quizView;
    private StatistikView statistikView;

    // Controller
    private VerwaltungsController verwaltungsController;
    private QuizController quizController;
    private StatistikController statistikController;

    // ✅ Theme + Ziel
    private final ThemeManager themeManager = new ThemeManager();
    private int goalTarget = 0; // 0 = nicht gesetzt

    public MainController(LernAnwendungModel model) {
        this.model = model;
    }

    public LernAnwendungModel getModel() {
        return model;
    }

    public void start() {
        frame = new MainFrame();

        // Controller
        verwaltungsController = new VerwaltungsController(this);
        quizController = new QuizController(this);
        statistikController = new StatistikController(this);

        // Views
        homeView = new HomeView(this);
        verwaltungView = new VerwaltungView(verwaltungsController);
        quizView = new QuizView(quizController);
        statistikView = new StatistikView(statistikController);

        // Controller -> View
        verwaltungsController.setView(verwaltungView);
        quizController.setView(quizView);
        statistikController.setView(statistikView);

        // Cards
        frame.addCard("HOME", homeView);
        frame.addCard("VERWALTUNG", verwaltungView);
        frame.addCard("QUIZ", quizView);
        frame.addCard("STATISTIK", statistikView);

        // Navigation
        frame.getBtnHome().addActionListener(e -> showHome());
        frame.getBtnVerwaltung().addActionListener(e -> showVerwaltung());
        frame.getBtnQuiz().addActionListener(e -> showQuiz());
        frame.getBtnStatistik().addActionListener(e -> showStatistik());

        frame.getBtnSpeichern().addActionListener(e -> speichernMitPopup());
        frame.getBtnBeenden().addActionListener(e -> beenden());

        // ✅ Theme Toggle
        frame.getBtnTheme().addActionListener(e -> {
            boolean dark = frame.getBtnTheme().isSelected();
            frame.getBtnTheme().setText(dark ? "Light Mode" : "Dark Mode");
            themeManager.apply(frame, dark ? ThemeManager.Theme.DARK : ThemeManager.Theme.LIGHT);
        });

        // Start
        showHome();
        updateGoalMini();

        frame.setVisible(true);
    }

    public void showHome() {
        homeView.refresh();
        frame.showCard("HOME");
    }

    public void showVerwaltung() {
        verwaltungsController.refresh();
        frame.showCard("VERWALTUNG");
    }

    public void showQuiz() {
        quizController.refresh();
        frame.showCard("QUIZ");
    }

    public void showStatistik() {
        statistikController.refresh();
        frame.showCard("STATISTIK");
    }

    public void speichernLeise() {
        model.getPool().speichern();
    }

    public void speichernMitPopup() {
        speichernLeise();
        JOptionPane.showMessageDialog(frame, "Gespeichert.");
    }

    public void beenden() {
        speichernLeise();
        frame.dispose();
        System.exit(0);
    }

    public JFrame getFrame() {
        return frame;
    }

    // ======================
    // ✅ Lernziel-API
    // ======================

    public int getGoalTarget() {
        return goalTarget;
    }

    public void setGoalTarget(int goalTarget) {
        this.goalTarget = goalTarget;
    }

    public int getTotalCorrectAll() {
        int sum = 0;
        for (Vokabeldatensatz v : model.getPool().getVokabeln()) {
            sum += v.getStatistik().getAnzahlRichtig();
        }
        return sum;
    }

    public void updateGoalMini() {
        int current = getTotalCorrectAll();
        if (goalTarget <= 0) {
            frame.setGoalMiniText("Ziel: nicht gesetzt");
        } else {
            frame.setGoalMiniText("Ziel: " + current + "/" + goalTarget);
        }
    }

    // Wenn irgendwo Statistik geändert wurde (Quiz), dann aufrufen:
    public void onStatsChanged() {
        updateGoalMini();
        // wenn Statistik-View gerade offen ist, refresh ist ok (sicher auch wenn nicht offen)
        if (statistikController != null) statistikController.refresh();
    }
}