package com.helpdesklite.service;

import com.helpdesklite.dto.*;
import com.helpdesklite.entity.Ticket;
import com.helpdesklite.enums.*;
import com.helpdesklite.exception.InvalidStatusTransitionException;
import com.helpdesklite.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {
 @Mock TicketRepository tickets;
 @Mock TicketStatusHistoryRepository histories;
 @InjectMocks TicketService service;
 Ticket ticket;
 @BeforeEach void setup(){
  ticket=new Ticket();ticket.setTitle("Cannot login");ticket.setDescription("Login issue");
  ticket.setPriority(Priority.HIGH);ticket.setStatus(TicketStatus.OPEN);
  ticket.setRequesterName("Test User");ticket.setRequesterEmail("test@example.com");
 }
 @Test void createsTicketHappyPath(){
  when(tickets.save(any(Ticket.class))).thenAnswer(i->i.getArgument(0));
  var result=service.create(new CreateTicketRequest("Cannot login","Login issue",Priority.HIGH,"Test User","test@example.com"));
  assertEquals("Cannot login",result.title());assertEquals(TicketStatus.OPEN,result.status());
  verify(tickets).save(any(Ticket.class));
 }
 @Test void rejectsInvalidStatusTransition(){
  when(tickets.findById(1L)).thenReturn(Optional.of(ticket));
  assertThrows(InvalidStatusTransitionException.class,()->service.updateStatus(1L,new UpdateStatusRequest(TicketStatus.CLOSED,"agent-1")));
  verify(tickets,never()).save(any(Ticket.class));verify(histories,never()).save(any());
 }
 @Test void recordsHistoryForValidStatusTransition(){
  when(tickets.findById(1L)).thenReturn(Optional.of(ticket));
  when(tickets.save(any(Ticket.class))).thenAnswer(i->i.getArgument(0));
  var result=service.updateStatus(1L,new UpdateStatusRequest(TicketStatus.IN_PROGRESS,"agent-1"));
  assertEquals(TicketStatus.IN_PROGRESS,result.status());verify(histories).save(any());
 }
}
