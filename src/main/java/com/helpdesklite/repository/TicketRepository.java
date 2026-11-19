package com.helpdesklite.repository;
import com.helpdesklite.entity.Ticket;
import com.helpdesklite.enums.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
public interface TicketRepository extends JpaRepository<Ticket,Long> {
 @Query("SELECT t FROM Ticket t WHERE (:status IS NULL OR t.status=:status) AND (:priority IS NULL OR t.priority=:priority) ORDER BY t.createdAt DESC")
 Page<Ticket> search(@Param("status") TicketStatus status,@Param("priority") Priority priority,Pageable pageable);
}
