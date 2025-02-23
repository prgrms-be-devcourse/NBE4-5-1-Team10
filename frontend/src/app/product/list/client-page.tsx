"use client";

import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import Combobox from "@/components/ui/custom/combobox";
import { components } from "@/lib/backend/generated/schema";
import { ShoppingCart } from "lucide-react";
import { useRouter } from "next/navigation";
import Image from "next/image";

export default function ClientPage({
  data,
  keyword,
  pageSize,
  page,
}: {
  data: components["schemas"]["RsDataItemsResBody"];
  keyword: string;
  pageSize: number;
  page: number;
}) {
  const router = useRouter();
  const products = data.data?.items;
  const totalPages = 1;
  const totalItems = data.data?.items?.length;
  const currentPageNo = 1;

  return (
    <div className="flex justify-center items-center w-full min-h-screen px-8 py-12">
      <Card
        className="
          w-full min-h-screen flex flex-col p-8 gap-6 flex-wrap content-start
          bg-white bg-gradient-to-t from-[rgba(0,0,0,0.001)] to-[rgba(0,0,0,0.001)]
          shadow-sm shadow-black/10
        "
      >
        <div className="w-full flex flex-col gap-6">
          <div className="w-full flex justify-center items-center">
            <p className="text-2xl font-semibold text-center">상품 목록</p>
          </div>
          <div className="flex w-full justify-end items-center mt-4 bg-transparent z-1">
            <Combobox
              itemList={[
                { value: "title", label: "상품번호순" },
                { value: "content", label: "인기순" },
              ]}
              title="상품번호 순"
            />
          </div>
          <div className="grid grid-cols-4 gap-6 w-full">
            {products?.map((product) => (
              <Card
                key={product.id}
                className="p-4 shadow-md border border-gray-200"
              >
                <div className="relative w-full h-40 overflow-hidden rounded-md">
                  <Image
                    src={product.imageUrl || ""}
                    alt={product.name || ""}
                    layout="fill"
                    objectFit="cover"
                    className="rounded-md"
                  />
                </div>
                <div className="mt-4 space-y-1">
                  <p className="text-lg font-medium">{product.name}</p>
                  <p className="text-xl font-bold">{product.price}</p>
                  <p
                    className={`text-sm ${
                      product.stockQuantity ? "text-green-600" : "text-red-600"
                    }`}
                  >
                    {product.stockQuantity ? "재고 있음" : "재고 없음"}
                  </p>
                </div>

                {/* 장바구니 버튼 */}
                <Button
                  className={`w-full mt-4 flex items-center gap-2 ${
                    product.stockQuantity
                      ? "bg-black text-white"
                      : "bg-gray-300 text-gray-500 cursor-not-allowed"
                  }`}
                  disabled={!product.stockQuantity}
                >
                  <ShoppingCart size={16} />
                  장바구니 담기
                </Button>
              </Card>
            ))}
          </div>
        </div>
      </Card>
    </div>
  );
}
