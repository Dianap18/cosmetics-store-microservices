package org.example.productservice.infrastructure;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.example.productservice.domain.IProductDAO;
import org.example.productservice.domain.Produs;
import org.example.productservice.domain.ProductID;
import org.example.productservice.infrastructure.tableEntities.ImagineEntity;
import org.example.productservice.infrastructure.tableEntities.ProdusEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductDAO implements IProductDAO {

    private final EntityManager entityManager;

    public ProductDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Produs> getToateProdusele() {
        List<ProdusEntity> entities = entityManager.createQuery("SELECT p FROM ProdusEntity p", ProdusEntity.class).getResultList();
        return entities.stream().map(ProdusEntity::ToProdus).collect(Collectors.toList());
    }

    @Override
    public Produs getProdusDupaDenumire(String denumire) {
        try {
            ProdusEntity entity = entityManager.createQuery(
                            "SELECT p FROM ProdusEntity p WHERE LOWER(p.denumire) LIKE LOWER(CONCAT('%', :denumire, '%'))", ProdusEntity.class)
                    .setParameter("denumire", denumire)
                    .setMaxResults(1)
                    .getSingleResult();

            return entity.ToProdus();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public boolean salveazaProdus(Produs produs) {
        try {
            ProdusEntity entity = new ProdusEntity(produs);
            entityManager.persist(entity);
            produs.setId(new ProductID(entity.getId()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean actualizeazaProdus(Produs produs) {
        try {
            ProdusEntity entity = entityManager.find(ProdusEntity.class, produs.getId().getId());
            if (entity == null) {
                return false;
            }

            entity.setDenumire(produs.getDenumire());
            entity.setProducator(produs.getProducator());
            entity.setPretAchizitie(produs.getPretAchizitie());
            entity.setPretVanzare(produs.getPretVanzare());

            if (entity.getImagini() != null) {
                entity.getImagini().clear();
            }

            if (produs.getImagini() != null) {
                for (String cale : produs.getImagini()) {
                    ImagineEntity img = new ImagineEntity();
                    img.setCaleFisier(cale);
                    img.setProdus(entity);
                    entity.getImagini().add(img);
                }
            }

            entityManager.merge(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean stergeProdus(int id) {
        try {
            ProdusEntity entity = entityManager.find(ProdusEntity.class, id);
            if (entity != null) {
                entityManager.remove(entity);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}