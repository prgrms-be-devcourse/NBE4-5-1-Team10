"use client";

import { useState } from "react";
import { Card } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Button } from "@/components/ui/button";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Trash, Upload } from "lucide-react";
import { useRouter } from "next/navigation";
import { components } from "@/lib/backend/generated/schema";

export default function ClientPage({
  product,
}: {
  product: components["schemas"]["ProductGetItemDto"];
}) {
  const [name, setName] = useState(product.name);
  const [description, setDescription] = useState(product.description);
  const [price, setPrice] = useState(String(product.price));
  const [stockQuantity, setStockQuantity] = useState(
    String(product.stockQuantity)
  );
  const [imageUrl, setImageUrl] = useState(product.imageUrl);
  const router = useRouter();

  const handleEditSubmit = async () => {
    const res = await fetch("/api/admin/product", {
      method: "PUT",
      body: JSON.stringify({
        id: product.id,
        name,
        description,
        price,
        imageUrl,
        stockQuantity,
      }),
    });
    if (!res.ok) {
      console.error("Update product failed:", res.status);
      return;
    }
    router.push("/admin/product/list");
  };

  const handleDeleteSubmit = async () => {
    const res = await fetch("/api/admin/product", {
      method: "DELETE",
      body: JSON.stringify({
        id: product.id,
      }),
    });
    if (!res.ok) {
      console.error("Delete product failed:", res.status);
      return;
    }
    router.push("/admin/product/list");
  };

  return (
    <div className="flex justify-center w-full min-h-screen px-8 py-12 bg-gray-100">
      <Card className="w-full max-w-screen-md p-8 bg-white shadow-md">
        <h2 className="text-2xl font-semibold text-center mb-6">상품 추가</h2>

        <div className="space-y-6">
          <div>
            <p className="text-sm font-medium mb-1">상품명</p>
            <Input value={name} onChange={(e) => setName(e.target.value)} />
          </div>

          <div>
            <p className="text-sm font-medium mb-1">설명</p>
            <Textarea
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
          </div>

          <div>
            <p className="text-sm font-medium mb-1">가격</p>
            <Input
              type="number"
              value={price}
              onChange={(e) => setPrice(e.target.value)}
            />
          </div>

          <div>
            <p className="text-sm font-medium mb-1">재고량</p>
            <Input
              type="number"
              value={stockQuantity}
              onChange={(e) => setStockQuantity(e.target.value)}
            />
          </div>

          <div>
            <p className="text-sm font-medium mb-1">이미지 링크</p>
            <Input
              value={imageUrl}
              onChange={(e) => setImageUrl(e.target.value)}
              placeholder="이미지 URL 입력"
            />
          </div>
        </div>

        <div className="flex justify-between mt-6">
          <Button
            variant="destructive"
            onClick={handleDeleteSubmit}
            className="flex items-center gap-2"
          >
            <Trash size={16} />
            삭제
          </Button>
          <Button onClick={handleEditSubmit} className="bg-black text-white">
            수정 완료
          </Button>
        </div>
      </Card>
    </div>
  );
}
