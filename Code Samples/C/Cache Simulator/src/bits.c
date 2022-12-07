#include "bits.h"
#include "cache.h"

//address_type = unsigned int
int get_set(Cache *cache, address_type address) {
  //  Extract the set bits from a 32-bit address.
  //
  int num_set_bits = cache->set_bits;
  int num_block_bits = cache->block_bits;
  int num_tag_bits = 32 - num_set_bits - num_block_bits;

  int mask = 0;
  int i;
  for (i = 0; i < num_tag_bits; i++) {
    mask = mask << 1;
    mask = mask | 1; 
  }

  for (i = 0; i < num_set_bits; i++) {
    mask = mask << 1;
  }

  for (i = 0; i < num_block_bits; i++) {
    mask = mask << 1;
    mask = mask | 1;
  }
  mask = ~mask;
  unsigned int set_bits = mask & address;
  set_bits = set_bits >> num_block_bits;
  return set_bits;
}

int get_tag(Cache *cache, address_type address) {
  // Extract the tag bits from a 32-bit address.
  //
  int num_set_bits = cache->set_bits;
  int num_block_bits = cache->block_bits;
  int tag_bits = address >> (num_block_bits + num_set_bits); 
  return tag_bits;
}

int get_byte(Cache *cache, address_type address) {
  // Extract the block offset (byte index) bits from a 32-bit address.
  //
  int num_set_bits = cache->set_bits;
  int num_block_bits = cache->block_bits;
  int num_tag_bits = 32 - num_set_bits - num_block_bits;
  unsigned int block_bits = address << (num_tag_bits + num_set_bits);
  block_bits = block_bits >> (num_tag_bits + num_set_bits);
  return block_bits;
}
