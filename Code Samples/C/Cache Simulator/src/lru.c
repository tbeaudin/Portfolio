#include "lru.h"
#include <stdio.h>
#include <stdlib.h>
#include "cache.h"

void lru_init_queue(Set *set) {
  LRUNode *s = NULL;
  LRUNode **pp = &s;  // place to chain in the next node
  for (int i = 0; i < set->line_count; i++) {
    Line *line = &set->lines[i];
    LRUNode *node = (LRUNode *)(malloc(sizeof(LRUNode)));
    node->line = line;
    node->next = NULL;
    (*pp) = node;
    pp = &((*pp)->next);
  }
  set->lru_queue = s;
}

void lru_init(Cache *cache) {
  Set *sets = cache->sets;
  for (int i = 0; i < cache->set_count; i++) {
    lru_init_queue(&sets[i]);
  }
}

void lru_destroy(Cache *cache) {
  Set *sets = cache->sets;
  for (int i = 0; i < cache->set_count; i++) {
    LRUNode *p = sets[i].lru_queue;
    LRUNode *n = p;
    while (p != NULL) {
      p = p->next;
      free(n);
      n = p;
    }
    sets[i].lru_queue = NULL;
  }
}

void lru_fetch(Set *set, unsigned int tag, LRUResult *result) {
  // Implement the LRU algorithm to determine which line in
  // the cache should be accessed.
  //

  LRUNode **node_head = &(set->lru_queue);
  LRUNode *curr_node = *node_head;
  LRUNode *prev_node = NULL;
  int length = 0;

  while (curr_node != NULL) {
    if ((curr_node->line)->valid == 1 && (curr_node->line)->tag == tag) {
      // Set result and move the node hit to the front of the queue, removing it from its current position
      result->line = curr_node->line;
      result->access = HIT;
      // Check to see if Cache hit is already head of the list
      if (*node_head == curr_node) {
        return;
      }

      prev_node->next = curr_node->next;
      curr_node->next = *node_head;
      *node_head = curr_node;
      return;
    }

    if ((curr_node->line)->valid == 1) {
      length++; // Could potentially be off, need to check later
    }
    prev_node = curr_node;
    curr_node = curr_node->next;
  }


  curr_node = *node_head;
  //prev_node = NULL;
  // Check to see if the set is full
  if (length == set->line_count) {
    (prev_node->line)->tag = tag;
    (prev_node->line)->valid = 1;
    result->line = prev_node->line;
    result->access = CONFLICT_MISS;
    prev_node->next = *node_head;
    while (curr_node->next != prev_node) {
      curr_node = curr_node->next;
    }
    curr_node->next = NULL;
    *node_head = prev_node;

    return;
  }
  else {
    while ((curr_node->line)->valid == 1) {
      prev_node = curr_node;
      curr_node = curr_node->next;
    }
    (curr_node->line)->tag = tag;
    (curr_node->line)->valid = 1;
    result->line = curr_node->line;
    result->access = COLD_MISS;

    // If the first node with valid bit 0 is the head no swapping necessary
    if (curr_node == *node_head) {
      return;
    }

    prev_node->next = curr_node->next;
    curr_node->next = *node_head;
    *node_head = curr_node;

    return;
  }
  
}
