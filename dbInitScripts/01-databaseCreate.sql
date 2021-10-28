CREATE TABLE public.Listing (
  id      serial primary key,
  "name"  varchar NOT NULL,
  price   numeric NOT NULL,
  address varchar NOT NULL,
  m2      int     NOT NULL
);
