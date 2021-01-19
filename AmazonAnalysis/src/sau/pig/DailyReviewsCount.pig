raw_data = load '/input' using PigStorage('\t')
            AS (marketplace, customer_id, review_id, product_id, product_parent, product_title, product_category, star_rating,
            helpful_votes, total_votes, vine, verified_purchase, review_headline, review_body, review_date);

data = STREAM raw_data THROUGH `tail -n +2`
        AS (marketplace, customer_id, review_id, product_id, product_parent, product_title, product_category, star_rating,
        helpful_votes, total_votes, vine, verified_purchase, review_headline, review_body, review_date);

daily = GROUP data by review_date;

daily_reviews = FOREACH daily GENERATE group as review_date, COUNT(data.review_id) as count;

order_by_data = ORDER daily_reviews BY count DESC;

store order_by_data INTO '/output/pig';
