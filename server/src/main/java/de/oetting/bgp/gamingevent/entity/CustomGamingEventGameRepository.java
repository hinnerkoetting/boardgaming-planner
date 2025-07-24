package de.oetting.bgp.gamingevent.entity;

import jakarta.persistence.EntityManager;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CustomGamingEventGameRepository extends SimpleJpaRepository<GamingEventGameEntity, Long> {

    private final EntityManager entityManager;

    public CustomGamingEventGameRepository(EntityManager entityManager) {
        super(GamingEventGameEntity.class, entityManager);
        this.entityManager = entityManager;
    }

    // We only want the first result, which does not seem possible with JPQL. This also seems difficult to load with spring data.
    // So we are back to using .setMaxResults(1)
    public Optional<GamingEventGameEntity> findByGameIdAndLastByOrderByGamingEventStart(long gameId, long playerId) {
        var resultList = entityManager.createQuery("from GamingEventGameEntity" +
                        " where game.id = :gameId" +
                        " and exists (select p from GamingEventParticipantsEntity p where p.gamingEvent = gamingEvent and p.participant.id = :playerId and p.participationStatus in ('CONFIRMED', 'MAYBE'))" +
                        " order by gamingEvent.start desc", GamingEventGameEntity.class)
                .setParameter("gameId", gameId)
                .setParameter("playerId", playerId)
                .setMaxResults(1)
                .getResultList();
        return Optional.ofNullable(DataAccessUtils.singleResult(resultList));
    }
}