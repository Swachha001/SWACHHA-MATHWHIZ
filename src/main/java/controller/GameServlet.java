package controller;

import dao.ScoreDAO;
import model.GameScore;
import model.Question;
import service.QuestionFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/game", "/leaderboard"})
public class GameServlet extends HttpServlet {

    private static final String DIFFICULTY = "NORMAL";
    private static final int LIMIT = 1_000_000;

    private final ScoreDAO dao = new ScoreDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/leaderboard".equals(req.getServletPath())) {
            try {
                req.setAttribute("scores", dao.getTop10());
            } catch (SQLException e) {
                throw new ServletException("Could not load leaderboard", e);
            }
            req.getRequestDispatcher("/leaderboard.jsp").forward(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if ("start".equals(action)) startGame(req, resp);
        else if ("submit".equals(action)) submitGame(req, resp);
        else resp.sendRedirect(req.getContextPath() + "/index.jsp");
    }

    private int intParam(HttpServletRequest req, String name, String label) {
        String v = req.getParameter(name);
        try {
            int n = Integer.parseInt(v == null ? "" : v.trim());
            if (Math.abs(n) > LIMIT) throw new IllegalArgumentException(label + " must be between -" + LIMIT + " and " + LIMIT + ".");
            return n;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(label + " must be an integer.");
        }
    }

    private void startGame(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("playerName");
        name = name == null ? "" : name.trim();
        try {
            if (name.isEmpty() || name.length() > 100) throw new IllegalArgumentException("Enter a player name (1-100 characters).");
            List<Question> qs = new ArrayList<>();
            qs.add(QuestionFactory.prime(intParam(req, "primeNum", "Prime number input")));
            qs.add(QuestionFactory.mod(intParam(req, "dividend", "Dividend"), intParam(req, "divisor", "Divisor")));
            for (int i = 1; i <= 2; i++) {
                String fn = req.getParameter("fn" + i);
                qs.add(QuestionFactory.trig(
                        intParam(req, "coef" + i, "Trig " + i + " integer"),
                        fn == null ? "" : fn,
                        intParam(req, "angle" + i, "Trig " + i + " angle")));
            }
            HttpSession session = req.getSession(true);
            session.setAttribute("playerName", name);
            session.setAttribute("questions", qs);
            req.setAttribute("questions", qs);
            req.setAttribute("playerName", name);
            req.getRequestDispatcher("/game.jsp").forward(req, resp);
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }

    @SuppressWarnings("unchecked")
    private void submitGame(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        List<Question> qs = session == null ? null : (List<Question>) session.getAttribute("questions");
        String name = session == null ? null : (String) session.getAttribute("playerName");
        if (qs == null || name == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        int total = 0;
        boolean[] correct = new boolean[qs.size()];
        String[] given = new String[qs.size()];
        String[] expected = new String[qs.size()];
        for (int i = 0; i < qs.size(); i++) {
            Question q = qs.get(i);
            String ans = req.getParameter("answer" + i);
            given[i] = ans == null ? "-" : ans.trim();
            expected[i] = QuestionFactory.expected(q);
            correct[i] = QuestionFactory.isCorrect(q, ans);
            if (correct[i]) total += q.getMarks();
        }

        try {
            dao.save(new GameScore(name, total, DIFFICULTY, new Timestamp(System.currentTimeMillis())));
        } catch (SQLException e) {
            throw new ServletException("Could not save score", e);
        }

        session.removeAttribute("questions");
        req.setAttribute("playerName", name);
        req.setAttribute("score", total);
        req.setAttribute("questions", qs);
        req.setAttribute("correct", correct);
        req.setAttribute("given", given);
        req.setAttribute("expected", expected);
        req.getRequestDispatcher("/result.jsp").forward(req, resp);
    }
}
