"use client";

import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import Combobox from "@/components/ui/custom/combobox";
import { components } from "@/lib/backend/generated/schema";
import { ShoppingCart, Star } from "lucide-react";
import { useRouter } from "next/navigation";
import Image from "next/image";

export default function ClientPage({
  product,
  reviews,
}: {
  product: components["schemas"]["ProductGetItemDto"];
  reviews: any;
}) {
  return (
    <div className="flex justify-center items-center w-full min-h-screen px-8 py-12">
      <Card className="w-full max-w-screen-lg p-8 bg-white shadow-md">
        <div className="flex flex-col md:flex-row gap-10">
          <div className="w-full md:w-1/2">
            <Image
              src={product.imageUrl || ""}
              alt={product.name || ""}
              width={500}
              height={400}
              className="w-full h-auto rounded-md object-cover"
              style={{ width: 500, height: 400 }}
            />
          </div>

          <div className="w-full md:w-1/2 flex flex-col justify-center">
            <h2 className="text-2xl font-semibold">{product.name}</h2>
            <p className="text-3xl font-bold mt-2">
              {Number(product.price).toLocaleString()}원
            </p>
            <p className="text-gray-600 mt-4">{product.description}</p>

            <Button className="w-full mt-6 flex items-center gap-2 bg-black text-white">
              <ShoppingCart size={16} />
              장바구니 담기
            </Button>
          </div>
        </div>

        <div className="mt-12">
          <h3 className="text-xl font-semibold mb-4">상품 리뷰</h3>
          <div className="space-y-4">
            {reviews.map((review: any) => (
              <Card
                key={review.id}
                className="p-4 flex flex-col md:flex-row items-start justify-between bg-gray-50 border"
              >
                <div>
                  <div className="flex items-center gap-1 text-yellow-500">
                    {Array.from({ length: review.rating }).map((_, i) => (
                      <Star key={i} size={16} fill="currentColor" />
                    ))}
                  </div>
                  <p className="font-medium">{review.user}</p>
                  <p className="text-gray-700 mt-1">{review.comment}</p>
                </div>
                <p className="text-gray-500 text-sm">{review.date}</p>
              </Card>
            ))}
          </div>
        </div>
      </Card>
    </div>
  );
}
