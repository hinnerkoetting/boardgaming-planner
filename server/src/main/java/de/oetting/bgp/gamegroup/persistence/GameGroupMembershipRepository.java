package de.oetting.bgp.gamegroup.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameGroupMembershipRepository extends JpaRepository<GameGroupMembership, GameGroup2PlayerId> {
}
