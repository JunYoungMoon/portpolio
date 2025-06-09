CREATE TABLE IF NOT EXISTS trading (
   id SERIAL PRIMARY KEY,
   trading_idx BIGINT,
   status VARCHAR(10),
    price DECIMAL(15,2),
    seller_idx BIGINT,
    buyer_idx BIGINT,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
    );

CREATE TABLE IF NOT EXISTS local_pg (
    id SERIAL PRIMARY KEY,
    tid VARCHAR(100),
    user_id VARCHAR(50),
    amount VARCHAR(20),
    order_no VARCHAR(50),
    status VARCHAR(10),
    message TEXT,
    created_at TIMESTAMP DEFAULT NOW()
    );