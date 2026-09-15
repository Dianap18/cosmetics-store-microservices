package org.example.userservice.infrastructure;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.example.userservice.domain.IUserDAO;
import org.example.userservice.domain.UserID;
import org.example.userservice.domain.Utilizator;
import org.example.userservice.infrastructure.tableEntities.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserDAO implements IUserDAO {

    private final EntityManager entityManager;

    public UserDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Utilizator> getTotiUtilizatorii() {
        List<UserEntity> entities = entityManager.createQuery("SELECT u FROM UserEntity u", UserEntity.class).getResultList();
        return entities.stream().map(UserEntity::toUtilizator).collect(Collectors.toList());
    }

    @Override
    public List<Utilizator> getUtilizatoriDupaRol(String rol) {
        List<UserEntity> entities = entityManager.createQuery(
                        "SELECT u FROM UserEntity u WHERE u.rol = :rol", UserEntity.class)
                .setParameter("rol", rol)
                .getResultList();
        return entities.stream().map(UserEntity::toUtilizator).collect(Collectors.toList());
    }

    @Override
    public Utilizator getUtilizatorDupaId(int id) {
        UserEntity entity = entityManager.find(UserEntity.class, id);
        if (entity != null) {
            return entity.toUtilizator();
        }
        return null;
    }

    @Override
    @Transactional
    public boolean salveazaUtilizator(Utilizator user) {
        try {
            if (user.getId() == null || user.getId().getId() == 0) {
                UserEntity entity = new UserEntity(user);
                entityManager.persist(entity);
                user.setId(new UserID(entity.getId()));
            } else {
                UserEntity entity = entityManager.find(UserEntity.class, user.getId().getId());
                if (entity != null) {
                    entity.setUsername(user.getUsername());
                    entity.setParola(user.getParola());
                    entity.setRol(user.getRol());
                    entity.setEmail(user.getEmail());
                    entity.setTelefon(user.getTelefon());
                    entity.setIdMagazin(user.getIdMagazin() > 0 ? user.getIdMagazin() : null);

                    entityManager.merge(entity);
                } else {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean stergeUtilizator(int id) {
        try {
            UserEntity entity = entityManager.find(UserEntity.class, id);
            if (entity != null) {
                entityManager.remove(entity);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Utilizator cautaDupaUsernameSiParola(String username, String parola) {
        try {
            UserEntity entity = entityManager.createQuery(
                            "SELECT u FROM UserEntity u WHERE (u.email = :username OR u.username = :username) AND u.parola = :parola",
                            UserEntity.class)
                    .setParameter("username", username)
                    .setParameter("parola", parola)
                    .getSingleResult();

            return entity.toUtilizator();
        } catch (NoResultException e) {
            return null;
        }
    }
}