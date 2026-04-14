package com.givinghand.ejb;

import com.givinghand.model.AppUser;
import com.givinghand.model.RegistrationRequest;
import com.givinghand.util.ValidationUtil;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Stateless
public class UserService {

    @PersistenceContext(unitName = "GivingHandPU")
    private EntityManager em;

    /**
     * Returns null on success, or return  failure.
     */
    public Map<String, String> register(RegistrationRequest req) {
        Map<String, String> error = new HashMap<String, String>();

        // checking required fields
        if (req.getEmail() == null || req.getEmail().isBlank()) {
            error.put("field", "email");
            error.put("message", "Email is required.");
            return error;
        }
        if (req.getPassword() == null || req.getPassword().isBlank()) {
            error.put("field", "password");
            error.put("message", "Password is required.");
            return error;
        }
        if (req.getName() == null || req.getName().isBlank()) {
            error.put("field", "name");
            error.put("message", "Full name is required.");
            return error;
        }
        if (req.getBirthday() == null || req.getBirthday().isBlank()) {
            error.put("field", "birthday");
            error.put("message", "Birthday is required.");
            return error;
        }
        if (req.getRole() == null || req.getRole().isBlank()) {
            error.put("field", "role");
            error.put("message", "Role is required.");
            return error;
        }

        // email format validation
        if (!ValidationUtil.isValidEmail(req.getEmail())) {
            error.put("field", "email");
            error.put("message", "Invalid email format.");
            return error;
        }

        // birthday format
        if (!ValidationUtil.isValidBirthday(req.getBirthday())) {
            error.put("field", "birthday");
            error.put("message", "Birthday must be a valid past date (format: YYYY-MM-DD).");
            return error;
        }

        // role validation
        if (!ValidationUtil.isValidRole(req.getRole())) {
            error.put("field", "role");
            error.put("message", "Role must be 'donor' or 'organization'.");
            return error;
        }

        // duplicate email
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(u) FROM AppUser u WHERE u.email = :email", Long.class);
        query.setParameter("email", req.getEmail());
        if (query.getSingleResult() > 0) {
            error.put("field", "email");
            error.put("message", "Email is already registered.");
            return error;
        }

        // Persist user
        AppUser user = new AppUser();
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword()); 
        user.setFullName(req.getName());
        user.setBirthday(LocalDate.parse(req.getBirthday()));
        user.setBio(req.getBio()); //can be empty
        user.setRole(req.getRole().toLowerCase());

        em.persist(user);
        return null; //success
    }
}