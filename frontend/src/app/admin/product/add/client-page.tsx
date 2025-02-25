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
import { Upload } from "lucide-react";
import { useRouter } from "next/navigation";

export default function ClientPage() {
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [price, setPrice] = useState("");
  const [stockQuantity, setStockQuantity] = useState("");
  const [imageUrl, setImageUrl] = useState("");
  const router = useRouter();

  const handleSubmit = async () => {
    const res = await fetch("/api/admin/product", {
      method: "POST",
      body: JSON.stringify({
        name,
        description,
        price,
        imageUrl,
        stockQuantity,
      }),
    });
    if (!res.ok) {
      console.error("Post product failed:", res.status);
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

        <Button
          onClick={handleSubmit}
          className="w-full mt-6 bg-black text-white"
        >
          추가하기
        </Button>
      </Card>
    </div>
  );
}
