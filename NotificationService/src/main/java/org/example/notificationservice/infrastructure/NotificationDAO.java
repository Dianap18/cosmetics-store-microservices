package org.example.notificationservice.infrastructure;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.example.notificationservice.domain.INotificationDAO;
import org.example.notificationservice.domain.Notificare;
import org.example.notificationservice.domain.NotificationID;
import org.example.notificationservice.infrastructure.tableEntities.NotificationEntity;
import org.springframework.stereotype.Repository;

@Repository
public class NotificationDAO implements INotificationDAO {

    private final EntityManager entityManager;

    public NotificationDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public boolean salveazaIstoric(Notificare notificare) {
        try {
            NotificationEntity entity = new NotificationEntity();
            entity.setIdUtilizator(notificare.getIdUtilizator());
            entity.setDataExpedierii(notificare.getDataExpedierii());
            entity.setTipContact(notificare.getTip().name());
            entity.setDestinatar(notificare.getDestinatar());
            entity.setMesaj(notificare.getMesaj());
            entity.setStatus(notificare.getStatus());

            entityManager.persist(entity);

            notificare.setId(new NotificationID(entity.getId()));

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}