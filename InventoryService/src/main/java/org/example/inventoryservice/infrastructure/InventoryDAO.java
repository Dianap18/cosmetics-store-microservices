package org.example.inventoryservice.infrastructure;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.inventoryservice.domain.*;
import org.example.inventoryservice.infrastructure.tableEntities.MagazinEntity;
import org.example.inventoryservice.infrastructure.tableEntities.StocEntity;
import org.example.inventoryservice.infrastructure.tableEntities.VanzareEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
public class InventoryDAO implements IInventoryDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int getStocCurent(StoreID idMagazin, int idProdus) {
        try {
            String sql = "SELECT s FROM StocEntity s WHERE s.magazin.idMagazin = :idMag AND s.idProdus = :idProd";
            StocEntity stoc = entityManager.createQuery(sql, StocEntity.class)
                    .setParameter("idMag", idMagazin.getId())
                    .setParameter("idProd", idProdus)
                    .getSingleResult();
            return stoc.getCantitate();
        } catch (NoResultException e) {
            return 0;
        }
    }

    @Override
    public List<Magazin> findMagazineCuStoc(int idProdus) {
        String sql = "SELECT s FROM StocEntity s WHERE s.idProdus = :idProd AND s.cantitate > 0";
        List<StocEntity> rezultate = entityManager.createQuery(sql, StocEntity.class)
                .setParameter("idProd", idProdus)
                .getResultList();

        return rezultate.stream()
                .map(s -> s.getMagazin().ToMagazin())
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateStoc(StoreID idMagazin, int idProdus, int cantitateNoua) {
        try {
            String sql = "SELECT s FROM StocEntity s WHERE s.magazin.idMagazin = :idMag AND s.idProdus = :idProd";
            List<StocEntity> rezultate = entityManager.createQuery(sql, StocEntity.class)
                    .setParameter("idMag", idMagazin.getId())
                    .setParameter("idProd", idProdus)
                    .getResultList();

            if (rezultate.isEmpty()) {
                MagazinEntity magazin = entityManager.find(MagazinEntity.class, idMagazin.getId());
                if (magazin == null) return false;

                StocEntity stocNou = new StocEntity();
                stocNou.setMagazin(magazin);
                stocNou.setIdProdus(idProdus);
                stocNou.setCantitate(cantitateNoua);
                entityManager.persist(stocNou);
            } else {
                StocEntity stocExistent = rezultate.get(0);
                stocExistent.setCantitate(cantitateNoua);
                entityManager.merge(stocExistent);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean inregistreazaVanzare(StoreID idMagazin, int idProdus, int cantitate) {
        try {
            String sql = "SELECT s FROM StocEntity s WHERE s.magazin.idMagazin = :idMag AND s.idProdus = :idProd";
            StocEntity stoc = entityManager.createQuery(sql, StocEntity.class)
                    .setParameter("idMag", idMagazin.getId())
                    .setParameter("idProd", idProdus)
                    .getSingleResult();

            if (stoc.getCantitate() >= cantitate) {
                stoc.setCantitate(stoc.getCantitate() - cantitate);
                entityManager.merge(stoc);

                VanzareEntity vanzare = new VanzareEntity();
                vanzare.setMagazin(stoc.getMagazin());
                vanzare.setIdProdus(idProdus);
                vanzare.setCantitateVanduta(cantitate);
                vanzare.setDataVanzarii(LocalDateTime.now());
                entityManager.persist(vanzare);
                return true;
            }
            return false;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public List<Magazin> getToateMagazinele() {
        return entityManager.createQuery("SELECT m FROM MagazinEntity m", MagazinEntity.class)
                .getResultList()
                .stream()
                .map(MagazinEntity::ToMagazin)
                .collect(Collectors.toList());
    }
}