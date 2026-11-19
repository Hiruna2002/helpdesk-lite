package com.helpdesklite.service;

import com.helpdesklite.dto.*;
import com.helpdesklite.entity.*;
import com.helpdesklite.enums.*;
import com.helpdesklite.exception.*;
import com.helpdesklite.repository.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {
 private final TicketRepository tickets;
 private final TicketStatusHistoryRepository histories;
 public TicketService(TicketRepository t,TicketStatusHistoryRepository h){tickets=t;histories=h;}

 @Transactional
 public TicketResponse create(CreateTicketRequest r){
  Ticket t=new Ticket(); t.setTitle(r.title().trim()); t.setDescription(r.description().trim());
  t.setPriority(r.priority()); t.setStatus(TicketStatus.OPEN); t.setRequesterName(r.requesterName().trim());
  t.setRequesterEmail(r.requesterEmail().trim().toLowerCase());
  return TicketResponse.from(tickets.save(t));
 }
 @Transactional(readOnly=true)
 public Page<TicketResponse> list(TicketStatus status,Priority priority,int page,int size){
  return tickets.search(status,priority,PageRequest.of(Math.max(page,0),Math.min(Math.max(size,1),100))).map(TicketResponse::from);
 }
 @Transactional(readOnly=true)
 public TicketResponse getById(Long id){return TicketResponse.from(find(id));}

 @Transactional
 public TicketResponse updateStatus(Long id,UpdateStatusRequest r){
  Ticket t=find(id); TicketStatus old=t.getStatus(), next=r.status();
  if(old==next) throw new InvalidStatusTransitionException("Ticket is already in status "+old);
  boolean allowed=switch(old){
   case OPEN -> next==TicketStatus.IN_PROGRESS;
   case IN_PROGRESS -> next==TicketStatus.RESOLVED;
   case RESOLVED -> next==TicketStatus.CLOSED;
   case CLOSED -> false;
  };
  if(!allowed) throw new InvalidStatusTransitionException("Invalid status transition: "+old+" -> "+next+". Allowed workflow: OPEN -> IN_PROGRESS -> RESOLVED -> CLOSED");
  t.setStatus(next);
  if(next==TicketStatus.RESOLVED)t.setResolvedAt(LocalDateTime.now());
  Ticket saved=tickets.save(t);
  histories.save(new TicketStatusHistory(saved,old,next,r.actorRef().trim()));
  return TicketResponse.from(saved);
 }
 @Transactional
 public TicketResponse assign(Long id,AssignTicketRequest r){Ticket t=find(id);t.setAssigneeRef(r.assigneeRef().trim());return TicketResponse.from(tickets.save(t));}
 @Transactional(readOnly=true)
 public List<HistoryResponse> history(Long id){
  if(!tickets.existsById(id))throw new ResourceNotFoundException("Ticket not found with id: "+id);
  return histories.findByTicket_IdOrderByChangedAtAsc(id).stream().map(HistoryResponse::from).toList();
 }
 private Ticket find(Long id){return tickets.findById(id).orElseThrow(()->new ResourceNotFoundException("Ticket not found with id: "+id));}
}
