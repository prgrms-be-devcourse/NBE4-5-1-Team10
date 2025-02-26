import ProductClientPage from "./client-page";
import client from "@/lib/backend/client";

export default async function Page({
  searchParams,
}: {
  searchParams: {
    keyword: string;
    pageSize: number;
    page: number;
  };
}) {
  const { keyword = "", pageSize = 10, page = 1 } = await searchParams;

  return (
    <ProductClientPage page={page} pageSize={pageSize} keyword={keyword} />
  );
}
