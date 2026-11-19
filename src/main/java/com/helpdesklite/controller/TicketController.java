package com.helpdesklite.controller;
import com.helpdesklite.dto.*;
import com.helpdesklite.enums.*;
import com.helpdesklite.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
 private final TicketService service;
 public TicketController(TicketService s){
  service=s;
 }

 @PostMapping
 @ResponseStatus(HttpStatus.CREATED)
 public TicketResponse create(@Valid @RequestBody CreateTicketRequest r){
  return service.create(r);
 }

 @GetMapping
 public Page<TicketResponse> list(
         @RequestParam(required=false) TicketStatus status,
         @RequestParam(required=false) Priority priority,
         @RequestParam(defaultValue="0") int page,
         @RequestParam(defaultValue="20") int size){
  return service.list(status,priority,page,size);
 }

 @GetMapping("/{id}")
 public TicketResponse get(@PathVariable Long id){
  return service.getById(id);
 }

 @PatchMapping("/{id}/status")
 public TicketResponse status(@PathVariable Long id,@Valid @RequestBody UpdateStatusRequest r){
  return service.updateStatus(id,r);
 }

 @PatchMapping("/{id}/assignee")
 public TicketResponse assign(@PathVariable Long id,@Valid @RequestBody AssignTicketRequest r){
  return service.assign(id,r);
 }

 @GetMapping("/{id}/history") public List<HistoryResponse> history(@PathVariable Long id){
  return service.history(id);
 }
}
