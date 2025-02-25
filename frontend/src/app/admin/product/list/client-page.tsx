"use client";

import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import {
  Table,
  TableHeader,
  TableRow,
  TableHead,
  TableBody,
  TableCell,
} from "@/components/ui/table";
import Image from "next/image";
import { components } from "@/lib/backend/generated/schema";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { isValidUrl } from "@/lib/utils";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";

export default function ClientPage() {
  const router = useRouter();
  const [deleteProductId, setDeleteProductId] = useState(0);
  const [products, setProducts] = useState<
    components["schemas"]["ProductGetItemsDto"][]
  >([]);
  const [deleteModalOpen, setDeleteModalOpen] = useState(false);

  const handleDeleteProduct = async () => {
    try {
      if (deleteProductId == 0) return;
      const res = await fetch("/api/admin/product", {
        method: "DELETE",
        body: JSON.stringify({ id: deleteProductId }),
      });
      if (!res.ok) {
        console.error("Delete product failed:", res.status);
        return;
      }
      setDeleteModalOpen(false);
      setDeleteProductId(0);
    } catch (error) {
      console.error(error);
    }
  };

  async function initFetchProducts() {
    try {
      const res = await fetch("/api/products", {
        method: "GET",
      });
      if (!res.ok) {
        console.error("Fetch data failed:", res.status);
        return;
      }
      const { data } = await res.json();
      const products =
        data.items as components["schemas"]["ProductGetItemsDto"][];
      setProducts(products);
    } catch (err) {
      console.error("상품을 가져오는 데에 실패했습니다.", err);
    }
  }

  useEffect(() => {
    initFetchProducts();
  }, [deleteModalOpen]);

  return (
    <div className="flex justify-center w-full min-h-screen px-8 py-12 bg-gray-100">
      <Card className="w-full max-w-screen-lg p-6 bg-white shadow-md">
        <div className="flex justify-between items-center mb-4">
          <h3 className="text-lg font-semibold">상품 목록</h3>
          <Button
            className="bg-black text-white"
            onClick={() => router.push("/admin/product/add")}
          >
            새 상품 등록
          </Button>
        </div>

        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>상품명</TableHead>
              <TableHead>가격</TableHead>
              <TableHead>재고</TableHead>
              <TableHead> </TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {products.map((product) => {
              const isValidImage =
                product.imageUrl &&
                product.imageUrl.length > 0 &&
                isValidUrl(product.imageUrl);

              const imageUrl = isValidImage ? product.imageUrl : "/none.jpg";
              return (
                <TableRow key={product.id}>
                  <TableCell className="text-left">
                    <div className="flex items-center gap-4">
                      <Image
                        src={imageUrl || "/none.jpg"}
                        alt={product.name}
                        width={50}
                        height={50}
                        className="rounded-md"
                        style={{ width: 50, height: 50 }}
                        onError={(e) =>
                          (e.currentTarget.style.display = "none")
                        }
                      />
                      <p>{product.name}</p>
                    </div>
                  </TableCell>
                  <TableCell>{product.price.toLocaleString()}</TableCell>
                  <TableCell>{product.stockQuantity}</TableCell>
                  <TableCell>
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => router.push("/admin/product/edit")}
                    >
                      수정
                    </Button>
                  </TableCell>
                  <TableCell>
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => {
                        setDeleteProductId(product.id);
                        setDeleteModalOpen(true);
                      }}
                    >
                      삭제
                    </Button>
                  </TableCell>
                </TableRow>
              );
            })}
          </TableBody>
        </Table>
      </Card>

      <Dialog open={deleteModalOpen} onOpenChange={setDeleteModalOpen}>
        <DialogContent className="sm:max-w-md" aria-describedby="">
          <DialogHeader>
            <DialogTitle>상품을 삭제하시겠습니까?</DialogTitle>
            <DialogDescription />
          </DialogHeader>
          <div className="flex justify-between mt-4">
            <Button variant="outline" onClick={() => setDeleteModalOpen(false)}>
              취소하기
            </Button>
            <Button
              className="bg-black text-white"
              onClick={(e) => {
                e.preventDefault();
                handleDeleteProduct();
              }}
            >
              삭제하기
            </Button>
          </div>
        </DialogContent>
      </Dialog>
    </div>
  );
}
