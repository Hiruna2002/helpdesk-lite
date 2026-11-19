package com.helpdesklite.repository;
import com.helpdesklite.entity.TicketStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface TicketStatusHistoryRepository extends JpaRepository<TicketStatusHistory,Long> {
 List<TicketStatusHistory> findByTicket_IdOrderByChangedAtAsc(Long ticketId);
}
