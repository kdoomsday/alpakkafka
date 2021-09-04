CREATE TABLE public.Listing (
  id      serial primary key,
  "name"  varchar NOT NULL,
  price   numeric NOT NULL,
  address varchar NOT NULL
);
